package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.util.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.*;

/**
 * Main client application for interacting with the music band collection server.
 * 
 * <p>This class provides a command-line interface for users to interact with
 * a remote music band collection server. It handles user input, command parsing,
 * network communication, and response processing.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Interactive REPL (Read-Eval-Print Loop) for command execution</li>
 *   <li>TCP-based communication with the server</li>
 *   <li>Command queue management for script execution</li>
 *   <li>Automatic reconnection handling</li>
 *   <li>Comprehensive error handling and user feedback</li>
 * </ul>
 * 
 * <p>The client supports various commands for managing music bands including
 * adding, removing, updating, and querying collections. It also supports
 * script execution for batch operations.</p>
 * 
 * @author hanqnero
 * @version 1.0
 */
public class ClientApplication {
    public static final boolean DEBUG = true;
    private final ConsoleManager console;
    private final Map<String, CommandFactory> factories;
    private final Map<String, ExecutionResultHandler> handlers;

    private TCPClient tcpClient;

    private final Deque<Command> commandsQueue = new ArrayDeque<>();

    /**
     * Constructs a new ClientApplication instance.
     * 
     * <p>Initializes the console manager, command factories, and result handlers.
     * The TCP client is not initialized here and must be done separately
     * via {@link #initTCPClient()}.</p>
     */
    public ClientApplication() {
        console = new ConsoleManager(System.in, System.out);
        this.factories = ClientFactoryRegistry.createFactoriesView();
        this.handlers = ClientFactoryRegistry.createHandlersView();
    }

    /**
     * Initializes the TCP client for server communication.
     * 
     * <p>Creates a new TCPClient instance for communicating with the server.
     * If initialization fails, prints an error message and throws a RuntimeException.</p>
     * 
     * @throws RuntimeException if TCP client initialization fails
     */
    public void initTCPClient() {
        try {
            tcpClient = new TCPClient();
        } catch (IOException e) {
            console.printlnErr("Could not start network stack.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Establishes connection to the server.
     * 
     * <p>Attempts to connect to the server using the TCP client.
     * If connection fails, prints an error message and stops the application.</p>
     */
    public void connect() {
        try {
            tcpClient.connect();
        } catch (IOException e) {
            console.printlnErr("Could not connect to server.");
            stop();
        }
    }

    /**
     * Main entry point for the client application.
     * 
     * <p>Initializes the client, establishes server connection, and starts
     * the interactive REPL. Handles connection errors gracefully and ensures
     * proper cleanup on shutdown.</p>
     * 
     * @param args command line arguments (currently unused)
     */
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
