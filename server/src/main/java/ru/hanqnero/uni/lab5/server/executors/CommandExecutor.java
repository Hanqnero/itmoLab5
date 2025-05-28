package ru.hanqnero.uni.lab5.server.executors;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.ServerApplication;

/**
 * Interface defining the contract for command execution on the server side.
 * <p>
 * CommandExecutor implementations are responsible for processing specific command types
 * and returning appropriate execution results. Each executor is designed to handle
 * a particular command (e.g., add, remove, update) and has access to the collection
 * manager for data operations and optionally the server instance for control operations.
 * </p>
 * 
 * <p>
 * Key responsibilities:
 * <ul>
 *   <li>Execute commands received from clients</li>
 *   <li>Validate command parameters and perform business logic</li>
 *   <li>Interact with the collection manager for data persistence</li>
 *   <li>Return structured execution results with status and messages</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Implementation notes:
 * <ul>
 *   <li>Executors should be stateless and thread-safe</li>
 *   <li>Error handling should be comprehensive with informative messages</li>
 *   <li>All data modifications should go through the CollectionManager</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see Command
 * @see ExecutionResult
 * @see CollectionManager
 */
public interface CommandExecutor {
    /**
     * Executes the provided command and returns the result.
     * <p>
     * This method processes the command according to its type and parameters,
     * performs the necessary operations on the collection or server state,
     * and returns a structured result indicating success or failure.
     * </p>
     * 
     * @param command the command to execute, must not be null
     * @return execution result containing status, message, and optional data
     * @throws IllegalArgumentException if the command is null or invalid
     * @throws RuntimeException if execution fails due to system errors
     */
    ExecutionResult execute(Command command);
    
    /**
     * Sets the collection manager for this executor.
     * <p>
     * The collection manager provides access to the music band collection
     * and handles persistence operations. This method is typically called
     * during executor initialization to provide the necessary dependencies.
     * </p>
     * 
     * @param collection the collection manager instance, must not be null
     * @throws IllegalArgumentException if collection is null
     */
    void setCollection(CollectionManager collection);
    
    /**
     * Sets the server application instance for this executor.
     * <p>
     * This method provides access to the server instance for executors that
     * need to perform server control operations (e.g., shutdown, status queries).
     * Default implementation does nothing as most executors don't need server access.
     * </p>
     * 
     * @param server the server application instance, may be null
     */
    default void setServer(ServerApplication server) {
        // Does nothing.
    }
}
