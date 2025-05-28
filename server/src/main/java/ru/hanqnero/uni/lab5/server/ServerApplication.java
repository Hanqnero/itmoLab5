package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.console.ServerCommands;
import ru.hanqnero.uni.lab5.server.console.ServerConsole;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.util.NetworkConfig;

import java.io.*;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main server application that manages the music band collection and handles client connections.
 * 
 * <p>This class serves as the entry point for the server-side application, coordinating between
 * the TCP server for network communication, the collection manager for data operations,
 * and command executors for processing client requests.</p>
 * 
 * <p>The server uses non-blocking I/O to handle multiple concurrent client connections
 * efficiently. It maintains a collection of MusicBand objects and processes various
 * commands like add, remove, update, and query operations.</p>
 * 
 * <p>The server also provides a console interface for server-side commands like
 * stopping the server or checking server status.</p>
 * 
 * @author hanqnero
 * @version 1.0
 */
public class ServerApplication {
    private final CommandInfo[] availableCommandInfo;
    private final Map<String, CommandExecutor> executors;
    private final ServerConsole console;
    private final Map<String, Consumer<ServerApplication>> serverCommands;
    private CollectionManager collection;

    private final TCPServer tcpServer = new TCPServer();

    /**
     * Constructs a new ServerApplication instance.
     * 
     * <p>Initializes all the required components including command executors,
     * server console, and server commands. The collection is not initialized
     * here and must be done separately via {@link #initCollection()}.</p>
     */
    public ServerApplication() {
        availableCommandInfo = CommandInfo.values();
        executors = ServerExecutorRegistry.createExecutorsView();
        console = new ServerConsole(System.in, System.out);
        serverCommands = ServerCommands.getAllCommands();
    }

    /**
     * Main entry point for the server application.
     * 
     * <p>Starts the server by initializing the collection, starting the TCP server,
     * and entering the main event loop. Handles any initialization errors gracefully
     * and ensures proper cleanup on shutdown.</p>
     * 
     * @param args command line arguments (currently unused)
     */
    public static void main(String[] args) {
        ServerApplication server = new ServerApplication();
        server.initCollection();

        try { server.initTCPServer();
        } catch (IOException e) {
            server.console.println("Could not start listening on port " + NetworkConfig.PORT);
            server.console.println(e.getMessage());
        }
        try {
            server.loop();
        } catch (IOException e) {
            server.console.println("Error inside server main loop");
        }
        server.closeConnection();
    }

    /**
     * Initializes the music band collection from persistent storage.
     * 
     * <p>Creates a new CollectionManager instance and attempts to restore
     * the collection from the CSV file specified by the TEST_VAR environment
     * variable. If restoration fails, starts with an empty collection.</p>
     * 
     * <p>Also configures all command executors with references to the
     * collection and server instance.</p>
     */
    public void initCollection() {
        var collection = new CollectionManager();
        this.collection = collection;
        collection.initialize("MUSIC_BANDS_FILE");
        executors.values().forEach(e -> {
            e.setCollection(collection);
            e.setServer(this);
        });
        console.println("Collection initialized");
    }

    /**
     * Processes a command from a client and returns the execution result.
     * 
     * <p>Looks up the appropriate command executor based on the command name
     * and delegates execution to it. Returns null if no executor is found
     * for the given command.</p>
     * 
     * @param command the command to execute
     * @return the execution result, or null if command is not supported
     */
    public ExecutionResult response(Command command) {
        var executor = this.executors.get(command.getName());

        if (executor == null) {
            return null;
        }
        return executor.execute(command);
    }

    /**
     * Gets information about all available commands.
     * 
     * @return array of CommandInfo objects describing available commands
     */
    public CommandInfo[] getAvailableCommandInfo() {
        return availableCommandInfo;
    }

    /**
     * Gets the collection manager instance.
     * 
     * @return the collection manager managing the music band collection
     */
    public CollectionManager getCollection() {
        return this.collection;
    }

    /**
     * Stops the server gracefully.
     * 
     * <p>Closes the TCP server connection and exits the application.
     * This method is typically called from server console commands.</p>
     */
    public void stop() {
        console.println("Stopping server...");
        closeConnection();
        System.exit(0);
    }

    /**
     * Closes the TCP server connection.
     * 
     * <p>Attempts to close the TCP server gracefully. If an error occurs
     * during closure, it is wrapped in a RuntimeException.</p>
     * 
     * @throws RuntimeException if an I/O error occurs during closure
     */
    public void closeConnection() {
        try {
            tcpServer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles server console commands.
     * 
     * <p>Checks if input is available on the server console and processes
     * any available commands. Commands are processed case-insensitively.</p>
     * 
     * @throws ServerConsole.ServerConsoleEOFException if EOF is reached on console
     */
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

    /**
     * Initializes the TCP server.
     * 
     * <p>Sets up the server socket channel for accepting client connections
     * on the configured hostname and port.</p>
     * 
     * @throws IOException if an I/O error occurs during server initialization
     */
    private void initTCPServer() throws IOException {
        tcpServer.init();
    }

    /**
     * Main server event loop.
     * 
     * <p>Continuously processes server console commands and handles client
     * network events using non-blocking I/O. The loop continues until
     * a stop signal is received from the console or an unrecoverable
     * error occurs.</p>
     * 
     * <p>Client connections are handled concurrently without blocking
     * the main loop or other client operations.</p>
     * 
     * @throws IOException if an I/O error occurs during the main loop
     */
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
