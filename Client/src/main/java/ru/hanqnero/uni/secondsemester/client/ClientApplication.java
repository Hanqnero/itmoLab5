package ru.hanqnero.uni.secondsemester.client;

import ru.hanqnero.uni.secondsemester.client.factories.concrete.*;
import ru.hanqnero.uni.secondsemester.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.client.handlers.concrete.*;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.*;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;
import ru.hanqnero.uni.secondsemester.commons.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.secondsemester.client.exceptions.SubtypeScanError;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.*;

public class ClientApplication {
    public static final boolean DEBUG = true;
    private final ConsoleManager console;

    private final CommandRegistry registry = new CommandRegistry();
    private final HandlerRegistry handlerRegistry = new HandlerRegistry();

    private TCPClient tcpClient;

    private final Deque<Command> commandsQueue = new ArrayDeque<>();

    public ClientApplication() {
        console = new ConsoleManager(System.in, System.out);

        // Add commands to registry
        registry.register(
                "Help",
                new HelpFactory(),
                "- Display list of commands"
        );
        handlerRegistry.register(HelpResult.class, new HelpResultHandler(console, this), "Help");

        registry.register(
                "Exit",
                new ExitFactory(),
                "- Stop client without saving any changes"
        );
        handlerRegistry.register(ExitResult.class, new ExitResultHandler(this), "Exit");

        registry.register(
                "Info",
                new InfoFactory(),
                "- Display information about collection"
        );
        handlerRegistry.register(InfoResult.class, new InfoResultHandler(console), "Info");

        registry.register(
                "Show",
                new ShowFactory(),
                "- Display list of every collection item"
        );
        handlerRegistry.register(ShowResult.class, new ShowResultHandler(console), "Show");

        registry.register(
                "Add",
                new AddFactory(console),
                "--[min|max] {Music Band} - Add element to collection"
        );
        handlerRegistry.register(AddResult.class, new AddResultHandler(console), "Add");

        registry.register(
                "Update",
                new UpdateFactory(console),
                "<id> {Music Band} - Update element in collection"
        );
        handlerRegistry.register(UpdateResult.class, new UpdateResultHandler(console), "Update");

        registry.register(
                "Remove",
                new RemoveFactory(console),
                "--[id <id>|--studio {Studio}] - Remove element with matching id from collection"
        );
        handlerRegistry.register(RemoveResult.class, new RemoveResultHandler(console), "Remove");

        registry.register(
                "Clear",
                new ClearFactory(),
                "- Remove all items from the collection"
        );
        handlerRegistry.register(ClearResult.class, new ClearResultHandler(console), "Clear");

        registry.register(
                "Script",
                new ScriptFactory(console),
                "<filename> - Execute list of commands from file"
        );
        handlerRegistry.register(ScriptResult.class, new ScriptResultHandler(console, this), "Script");

        registry.register(
                "Save",
                new SaveFactory(),
                "- Save collection to text file"
        );
        handlerRegistry.register(SaveResult.class, new SaveResultHandler(console), "Save");

        registry.register(
                "RemoveGreater",
                new RemoveGreaterFactory(console),
                "{Music Band} - Remove all elements exceeding this from collection"
        );
        handlerRegistry.register(RemoveGreaterResult.class, new RemoveGreaterResultHandler(console), "RemoveGreater");

        registry.register(
                "Get",
                new GetFactory(),
                "--[min|max] --[creation|establishment]- Display first or last element after chosen sorting"
        );
        handlerRegistry.register(GetByResult.class, new GetResultHandler(console), "Get");
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
            console.printlnErr("Null response received");
            return;
        }

        var entry = handlerRegistry.find(response.getClass());
        if (entry.isEmpty()) {
            console.printlnErr("Cannot handle response of type %s".formatted(response.getClass().toString()));
            return;
        }

        ExecutionResultHandler handler = entry.get().handler();
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

    public Collection<CommandRegistry.RegistryEntry> getCommandRegistryEntries() {
        return registry.getEntries();
    }

}
