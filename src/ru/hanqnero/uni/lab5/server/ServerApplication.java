package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.console.ServerCommands;
import ru.hanqnero.uni.lab5.server.console.ServerConsole;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.function.Consumer;

public class ServerApplication {
    private final CommandInfo[] availableCommandInfo;
    private final Map<String, CommandExecutor> executors;
    private CollectionManager collection;
    private final ServerConsole console;
    private final Map<String, Consumer<ServerApplication>> serverCommands;
    private Socket clientSocket;

    public ServerApplication() {
        availableCommandInfo = CommandInfo.values();
        executors = CommandInfo.createExecutorsView(availableCommandInfo);
        console = new ServerConsole(System.in, System.out);
        serverCommands = ServerCommands.getAllCommands();

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
        console.println("Stopping server...");
        System.exit(0);
    }

    public void establishConnection(int port) {
        console.println("Server is listening on port 4444...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.clientSocket = serverSocket.accept();
            console.println("Established connection with %s"
                    .formatted(clientSocket.getInetAddress())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientSocket = null;
    }

    public void loop() {
        while (true) {
            try {
                if (console.ready()) {
                    String command = console.readLine().trim().toLowerCase();
                    serverCommands.getOrDefault(command, s -> {
                        s.console.println("Command with name `%s` not found.".formatted(command));
                        s.console.println("Available server commands: %s".formatted(
                                String.join(", ", serverCommands.keySet())));
                    }).accept(this);
                }
            } catch (ServerConsole.ServerConsoleEOFException e) {
                break;
            }

            try {
                if (clientSocket == null) {
                    establishConnection(16482);
                }

                var ois = new ObjectInputStream(clientSocket.getInputStream());
                Command commandDTO = (Command) ois.readObject();

                ExecutionResult resultDTO = response(commandDTO);

                var oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject(resultDTO);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                console.println("ERROR: CommandDTO is unknown type.");
                // TODO: Возможно тут нужно отравить что-то клиенту
            }
        }
        console.println("Console was terminated, stopping server...");
        stop();
    }

    public static void main(String[] args) {
        ServerApplication server = new ServerApplication();
        server.initializeCollection();
        server.loop();
    }
}
