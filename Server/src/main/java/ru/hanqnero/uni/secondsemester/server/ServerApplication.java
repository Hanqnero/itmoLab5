package ru.hanqnero.uni.secondsemester.server;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.*;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.server.console.ServerCommands;
import ru.hanqnero.uni.secondsemester.server.console.ServerConsole;
import ru.hanqnero.uni.secondsemester.server.executors.CommandExecutor;
import ru.hanqnero.uni.secondsemester.server.executors.concrete.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ServerApplication {
    private final Map<Class<? extends Command>, CommandExecutor> executors = new HashMap<>();
    private final ServerConsole console;
    private final Map<String, Consumer<ServerApplication>> serverCommands;
    private CollectionManager collection;

    private final TCPServer tcpServer = new TCPServer();

    public ServerApplication() {
        console = new ServerConsole(System.in, System.out);
        serverCommands = ServerCommands.getAllCommands();
        executors.put(AddCommand.class, new AddExecutor(collection));
        executors.put(ClearCommand.class, new ClearExecutor(collection));
        executors.put(ExitCommand.class, new ExitExecutor());
        executors.put(GetByDateCommand.class, new GetByDateExecutor(collection));
        executors.put(HelpCommand.class, new HelpExecutor());
        executors.put(InfoCommand.class, new InfoExecutor(collection));
        executors.put(RemoveCommand.class, new RemoveExecutor(collection));
        executors.put(RemoveStudio.class, new RemoveExecutor(collection));
        executors.put(RemoveGreaterCommand.class, new RemoveGreaterExecutor(collection));
        executors.put(SaveCommand.class, new SaveExecutor(collection));
        executors.put(ScriptCommand.class, new ScriptExecutor());
        executors.put(ShowCommand.class, new ShowExecutor(collection));
        executors.put(UpdateCommand.class, new UpdateExecutor(collection));
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
        console.println("Collection initialized");
    }

    public ExecutionResult response(Command command) {
        var executor = this.executors.get(command.getClass());

        if (executor == null) {
            return null;
        }
        return executor.execute(command);
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
