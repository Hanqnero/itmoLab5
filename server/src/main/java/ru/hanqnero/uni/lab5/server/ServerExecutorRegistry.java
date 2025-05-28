package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.server.executors.concrete.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for managing and providing access to command executors on the server side.
 * <p>
 * ServerExecutorRegistry serves as a central factory and registry for all available
 * command executors in the server application. It provides a unified way to access
 * executor instances based on command names, implementing the Registry pattern to
 * manage the mapping between command identifiers and their corresponding executors.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Centralized executor management and instantiation</li>
 *   <li>Static factory method for executor map creation</li>
 *   <li>Type-safe mapping between command names and executors</li>
 *   <li>Easy addition of new command executors</li>
 *   <li>Consistent executor lifecycle management</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The registry creates a complete mapping of all supported commands to their
 * corresponding executor implementations. This design allows the server to
 * dynamically dispatch commands to appropriate handlers while maintaining
 * a clean separation of concerns.
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see CommandExecutor
 * @see CommandInfo
 */
public class ServerExecutorRegistry {
    
    /**
     * Creates and returns a complete mapping of command names to their executor instances.
     * <p>
     * This factory method instantiates all available command executors and creates
     * a HashMap mapping from command names (as defined in CommandInfo) to their
     * corresponding executor implementations. The returned map provides the server
     * with a complete registry of all supported operations.
     * </p>
     * 
     * <p>
     * Supported commands include:
     * <ul>
     *   <li>HELP - Display available commands and their descriptions</li>
     *   <li>EXIT - Gracefully shut down the client connection</li>
     *   <li>INFO - Display collection information and statistics</li>
     *   <li>SHOW - Display all elements in the collection</li>
     *   <li>ADD - Add a new music band to the collection</li>
     *   <li>UPDATE - Update an existing music band by ID</li>
     *   <li>REMOVE - Remove a music band by ID</li>
     *   <li>CLEAR - Remove all elements from the collection</li>
     *   <li>SCRIPT - Execute commands from a script file</li>
     *   <li>SAVE - Save the collection to persistent storage</li>
     *   <li>REMOVE_GREATER - Remove bands with more participants than specified</li>
     *   <li>GET_BY - Retrieve bands by specific criteria</li>
     * </ul>
     * </p>
     * 
     * @return a new HashMap containing mappings from command names to executor instances
     * @throws RuntimeException if executor instantiation fails
     */
    public static Map<String, CommandExecutor> createExecutorsView() {
        Map<String, CommandExecutor> executors = new HashMap<>();
        executors.put(CommandInfo.HELP.getName(), new HelpExecutor());
        executors.put(CommandInfo.EXIT.getName(), new ExitExecutor());
        executors.put(CommandInfo.INFO.getName(), new InfoExecutor());
        executors.put(CommandInfo.SHOW.getName(), new ShowExecutor());
        executors.put(CommandInfo.ADD.getName(), new AddExecutor());
        executors.put(CommandInfo.UPDATE.getName(), new UpdateExecutor());
        executors.put(CommandInfo.REMOVE.getName(), new RemoveExecutor());
        executors.put(CommandInfo.CLEAR.getName(), new ClearExecutor());
        executors.put(CommandInfo.SCRIPT.getName(), new ScriptExecutor());
        executors.put(CommandInfo.SAVE.getName(), new SaveExecutor());
        executors.put(CommandInfo.REMOVE_GREATER.getName(), new RemoveGreaterExecutor());
        executors.put(CommandInfo.GET_BY.getName(), new GetByExecutor());
        return executors;
    }
}
