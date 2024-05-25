package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.console.ServerCommands;
import ru.hanqnero.uni.lab5.server.console.ServerConsole;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
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
    private Socket clientSocket;
    private ServerSocketChannel serverSocket;
    private Selector selector;

    public ServerApplication() {
        availableCommandInfo = CommandInfo.values();
        executors = CommandInfo.createExecutorsView(availableCommandInfo);
        console = new ServerConsole(System.in, System.out);
        serverCommands = ServerCommands.getAllCommands();
    }

    public static void main(String[] args) {
        ServerApplication server = new ServerApplication();

        int port;
        try { port = Integer.parseInt(args[0]); }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException e) { port = 16482; }

        server.initializeCollection();

        try { server.initSelectorWithSocket(port);
        } catch (IOException e) {
            server.console.println("Could not start listening on port " + port);
            server.console.println(e.getMessage());
        }

        server.loop();
        server.closeConnection();

    }

    public void initializeCollection() {
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
        // TODO
        console.println("Stopping server...");
        System.exit(0);
    }

    public void closeConnection() {
        // TODO
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientSocket = null;
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

    private void initSelectorWithSocket(int port) throws IOException {
        serverSocket = ServerSocketChannel.open();
        serverSocket
                .bind(new InetSocketAddress(port))
                .configureBlocking(false);
        selector = Selector.open();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void register() throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void answer(SelectionKey key) {
        try {
            SocketChannel client = (SocketChannel) key.channel();

            var buf = ByteBuffer.allocate(2048);
            client.read(buf);
            buf.flip();

            var bis = new ByteArrayInputStream(buf.array());
            var ois = new ObjectInputStream(bis);

            Command command = (Command) ois.readObject();
            ExecutionResult result = response(command);

            var bos = new ByteArrayOutputStream();
            var oos = new ObjectOutputStream(bos);

            oos.writeObject(result);
            oos.flush();
            oos.close();

            buf = ByteBuffer.wrap(bos.toByteArray());
            client.write(buf);

            client.close();
        } catch (IOException e) {
            console.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            console.println("Error while deserializing command object.");
        }
    }

    public void loop() {
        while (true) {
            try {
                handleServerCommand();
            } catch (ServerConsole.ServerConsoleEOFException e) {
                console.println("Received stop signal from console");
                break;
            }
            try {
                selector.selectNow();
                var keysIter = selector.selectedKeys().iterator();
                if (!keysIter.hasNext()) {
                    continue;
                }
                SelectionKey key = keysIter.next();

                if (key.isAcceptable()) {
                    register();
                } else if (key.isReadable()) {
                    answer(key);
                    keysIter.remove();
                }
            } catch (IOException e) {
                console.println(e.getMessage());
            }
        }
    }
}
