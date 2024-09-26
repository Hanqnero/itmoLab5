package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.CommandInfo;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.util.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.lab5.commons.util.exceptions.SubtypeScanError;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.*;

public class ClientApplication {
    public static final boolean DEBUG = true;
    private final ConsoleManager console;
    private final Map<String, CommandFactory> factories;
    private final Map<String, ExecutionResultHandler> handlers;

    private TCPClient tcpClient;

    private final Deque<Command> commandsQueue = new ArrayDeque<>();

    public ClientApplication() {
        console = new ConsoleManager(System.in, System.out);
        this.factories = CommandInfo.createFactoriesView();
        this.handlers = CommandInfo.createHandlersView();
    }

    public void initTCPClient() {
        try {
            tcpClient = new TCPClient();
        } catch (IOException e) {
            console.printlnErr("Could not start network stack.");
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        try {
            tcpClient.connect();
        } catch (IOException e) {
            console.printlnErr("Could not connect to server.");
            stop();
        }
    }

    public static void main(String[] args) {
        ClientApplication app = new ClientApplication();

        app.initTCPClient();

        app.connect();

        try {
            app.repl();
        } catch (ClosedChannelException e) {
            app.console.printlnErr("Server closed connection. Probably server is offline now.");
        } catch (IOException e) {
            app.console.printlnErr("Error inside main loop");
        }
        app.stop();
    }

    public Optional<Command> createCommandFromTokens(String[] tokens, ConsoleManager console)
            throws CommandCreationError, SubtypeScanError {
        var factory = this.factories.get(tokens[0]);
        if (factory == null) {
            return Optional.empty();
        }

        factory.setConsole(console);
        return Optional.ofNullable(factory.createCommand(tokens));
    }

    public Optional<Command> readCommand() throws ConsoleEmptyException {
        String line;
        line = console.nextLine();
        if (line.isEmpty()) {
            return Optional.empty();
        }

        String[] tokens = line.split(" ");

        assert tokens.length > 0;

        Optional<Command> commandOptional;
        try {
            commandOptional = createCommandFromTokens(tokens, console);
        } catch (SubtypeScanError | CommandCreationError e) {
            return Optional.empty();
        }
        if (commandOptional.isEmpty()) {
            console.printlnErr("No such command `%s`".formatted(tokens[0]));
        }
        return commandOptional;
    }

    public void handleResponse(ExecutionResult response) {
        if (response == null) {
            console.printlnErr("Cannot handle null execution result");
            return;
        }

        if (!(handlers.containsKey(response.getCommandName()))) {
            console.printlnErr("Cannot handle received response for command `%s`".formatted(response.getCommandName()));
            return;
        }
        ExecutionResultHandler handler = handlers.get(response.getCommandName());
        handler.setConsole(console);
        handler.setClient(this);
        handler.handleResult(response);
    }

    public void readCommandToQueue() throws ConsoleEmptyException {
        Optional<Command> command = readCommand();
        command.ifPresent(commandsQueue::add);
    }

    public void repl() throws IOException {
        while (true) {
            try {
                if (commandsQueue.isEmpty()) {
                    readCommandToQueue();
                }
            } catch (ConsoleEmptyException e) {
                break;
            }

            Command command = commandsQueue.peek();
            if (command == null) {
                continue;
            }

            // Server Command sending and receiving result
            if (!tcpClient.ensureConnected()) {
                break;
            }
            tcpClient.send(command);
            Optional<ExecutionResult> receivedResult = tcpClient.receive();
            if (receivedResult.isPresent()) {
                commandsQueue.remove();
                handleResponse(receivedResult.get());
            }
        }
        console.printlnWarn("EOF reached. exiting REPL...");
    }

    public void closeConnection() {
        try {
            tcpClient.close();
            console.println("Successfully closed network connection.");
        } catch (IOException e) {
            console.printlnErr("Could not close network connection properly");
        }
    }

    public void stop() {
        closeConnection();
        System.exit(0);
    }

    public void addCommandsToQueue(Queue<Command> commands) {
        // Need to add all commands from script in right order BEFORE all current commands in queue.
        commands.iterator().forEachRemaining(commandsQueue::push);
    }
}
