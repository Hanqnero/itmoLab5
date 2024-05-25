package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.console.ServerCommands;
import ru.hanqnero.uni.lab5.server.console.ServerConsole;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.function.Consumer;

public class ServerApplication {
    private final CommandInfo[] availableCommandInfo;
    private final Map<String, CommandExecutor> executors;
    private final ServerConsole console;
    private final Map<String, Consumer<ServerApplication>> serverCommands;
    private CollectionManager collection;

    private final TCPServer tcpServer = new TCPServer();

    public ServerApplication() {
        availableCommandInfo = CommandInfo.values();
        executors = CommandInfo.createExecutorsView(availableCommandInfo);
        console = new ServerConsole(System.in, System.out);
        serverCommands = ServerCommands.getAllCommands();
    }

    public static void main(String[] args) {
        ServerApplication server = new ServerApplication();
        server.initCollection();

        try { server.initTCPServer();
        } catch (IOException e) {
            server.console.println("Could not start listening on port " + TCPServer.PORT);
            server.console.println(e.getMessage());
        }
        try {
            server.loop();
        } catch (IOException e) {
            server.console.println("Error inside server main loop");
        }
        server.closeConnection();
    }

    public void initCollection() {
        var collection = new CollectionManager();
        this.collection = collection;
        collection.initialize("TEST_VAR");
        executors.values().forEach(e -> {
            e.setCollection(collection);
            e.setServer(this);
        });
        console.println("Collection initialized");
    }

    public ExecutionResult response(Command command) {
        var executor = this.executors.get(command.getName());

        if (executor == null) {
            return null;
        }
        return executor.execute(command);
    }

    public CommandInfo[] getAvailableCommandInfo() {
        return availableCommandInfo;
    }

    public CollectionManager getCollection() {
        return this.collection;
    }

    public void stop() {
        console.println("Stopping server...");
        closeConnection();
        System.exit(0);
    }

    public void closeConnection() {
        try {
            tcpServer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleServerCommand() throws ServerConsole.ServerConsoleEOFException {
        if (console.ready()) {
            String command = console.readLine().trim().toLowerCase();
            serverCommands.getOrDefault(command, s -> {
                s.console.println("Command with name `%s` not found.".formatted(command));
                s.console.println("Available server commands: %s".formatted(
                        String.join(", ", serverCommands.keySet())));
            }).accept(this);
        }
    }

    private void initTCPServer() throws IOException {
        tcpServer.init();
    }

    private void register(SelectionKey key) {
        try (var serverSocket = (ServerSocketChannel) key.channel()){

            SocketChannel client = serverSocket.accept();
            client.configureBlocking(false);
            client.register(null, SelectionKey.OP_READ);
            console.println("Accepted a connection from client on address " + client.getLocalAddress());
        } catch (IOException e) {
            console.println("Could not accept connection from a client.");
        }
    }

    private void answer(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();

            var buf = ByteBuffer.allocate(2048);
            client.read(buf);
            buf.flip();

            console.println("Got buffer from client");
            console.println(buf.toString());


            var bis = new ByteArrayInputStream(buf.array());
            var ois = new ObjectInputStream(bis);

            Command command = (Command) ois.readObject();

            console.println("Deserialized command from buffer " + command.toString());

            ExecutionResult result = response(command);

            console.println("Starting serializing response " + result.toString());

            var bos = new ByteArrayOutputStream();
            var oos = new ObjectOutputStream(bos);

            oos.writeObject(result);
            oos.flush();
            oos.close();

            buf = ByteBuffer.wrap(bos.toByteArray());
            buf.flip();

            console.println("Serialized response " + buf);

            client.write(buf);

            console.println("Successfully wrote response to channel");

            client.close();
            console.println("Closed client channel on server side");

        } catch (IOException e) {
            console.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            console.println("Error while deserializing command object.");
        }
    }

    public void loop() throws IOException {
        while (true) {
            try {
                handleServerCommand();
            } catch (ServerConsole.ServerConsoleEOFException e) {
                console.println("Received stop signal from console");
                break;
            }

            var keysIter = tcpServer.selectNowIter();

            while (keysIter.hasNext()) {
                var key = keysIter.next();
                tcpServer.handleKey(key, this::response);
                keysIter.remove();
            }

        }
    }
}
