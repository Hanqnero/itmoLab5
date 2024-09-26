package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.commons.util.CommandInfo;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.console.ServerCommands;
import ru.hanqnero.uni.lab5.server.console.ServerConsole;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

import java.io.*;
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
