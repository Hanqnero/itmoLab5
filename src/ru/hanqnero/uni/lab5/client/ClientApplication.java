package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.client.factories.concrete.RemoveGreaterFactory;
import ru.hanqnero.uni.lab5.client.factories.concrete.*;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.client.handlers.concrete.*;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.lab5.commons.exceptions.SubtypeScanError;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.*;

public class ClientApplication {
    public static final boolean DEBUG = true;
    private final ConsoleManager console;

    private final CommandRegistry registry = new CommandRegistry();

    private TCPClient tcpClient;

    private final Deque<Command> commandsQueue = new ArrayDeque<>();

    public ClientApplication() {
        console = new ConsoleManager(System.in, System.out);

        // Add commands to registry
        registry.register(
                "Help",
                new HelpFactory(),
                new HelpResultHandler(),
                "- Display list of commands"
        );

        registry.register(
                "Exit",
                new ExitFactory(),
                new ExitResultHandler(),
                "- Stop client without saving any changes"
        );

        registry.register(
                "Info",
                new InfoFactory(),
                new InfoResultHandler(),
                "- Display information about collection"
        );

        registry.register(
                "Show",
                new ShowFactory(),
                new ShowResultHandler(),
                "- Display list of every collection item"
        );

        registry.register(
                "Add",
                new AddFactory(console),
                new AddResultHandler(),
                "--[min|max] {Music Band} - Add element to collection"
        );

        registry.register(
                "Update",
                new UpdateFactory(console),
                new UpdateResultHandler(),
                "<id> {Music Band} - Update element in collection"
        );

        registry.register(
                "Remove",
                new RemoveFactory(console),
                new RemoveResultHandler(),
                "--[id <id>|--studio {Studio}] - Remove element with matching id from collection"
        );

        registry.register(
                "Clear",
                new ClearFactory(),
                new ClearResultHandler(),
                "- Remove all items from the collection"
        );

        registry.register(
                "Clear",
                new ClearFactory(),
                new ClearResultHandler(),
                "- Remove all items from the collection"
        );

        registry.register(
                "Script",
                new ScriptFactory(console),
                new ScriptResultHandler(),
                "<filename> - Execute list of commands from file"
        );

        registry.register(
                "Save",
                new SaveFactory(),
                new SaveResultHandler(),
                "- Save collection to text file"
        );

        registry.register(
                "RemoveGreater",
                new RemoveGreaterFactory(console),
                new RemoveGreaterResultHandler(),
                "{Music Band} - Remove all elements exceeding this from collection"
        );

        registry.register(
                "Get",
                new GetFactory(),
                new GetResultHandler(),
                "--[min|max] --[creation|establishment]- Display first or last element after chosen sorting"
        );
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
        var entry = registry.findEntry(tokens[0]);
        if (entry.isEmpty())
            return Optional.empty();
        var factory = entry.get().commandFactory();
        return Optional.of(factory.createCommand(tokens));
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
