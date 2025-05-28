package ru.hanqnero.uni.lab5.client.handlers;

import ru.hanqnero.uni.lab5.client.ClientApplication;
import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

/**
 * Interface defining the contract for handling execution results from server commands.
 * <p>
 * ExecutionResultHandler implementations are responsible for processing ExecutionResult
 * objects received from the server and presenting them to the user in an appropriate
 * format. Each handler is specialized for a specific command type and knows how to
 * format and display the particular type of result data that command produces.
 * </p>
 * 
 * <p>
 * Key responsibilities:
 * <ul>
 *   <li>Process ExecutionResult objects from the server</li>
 *   <li>Format and display results to the user</li>
 *   <li>Handle error conditions and status messages</li>
 *   <li>Provide appropriate user feedback based on command type</li>
 *   <li>Integrate with console for output operations</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Handler pattern benefits:
 * <ul>
 *   <li>Command-specific result processing logic</li>
 *   <li>Consistent error handling across result types</li>
 *   <li>Separation of concerns between command execution and result presentation</li>
 *   <li>Easy extension with new command types and result formats</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see ExecutionResult
 * @see ConsoleManager
 * @see ClientApplication
 */
public interface ExecutionResultHandler {
    /**
     * Handles the execution result received from the server.
     * <p>
     * This method processes the ExecutionResult object, extracts relevant information,
     * and presents it to the user in an appropriate format. The handling logic is
     * specific to the command type and may include formatting data, displaying error
     * messages, or performing additional client-side operations.
     * </p>
     * 
     * @param result the execution result from the server, must not be null
     * @throws IllegalArgumentException if result is null
     * @throws RuntimeException if handling fails due to system errors
     */
    void handleResult(ExecutionResult result);
    
    /**
     * Sets the console manager for output operations.
     * <p>
     * The console manager is used to display results, error messages, and other
     * user feedback. This method is typically called during handler initialization
     * to provide the necessary dependencies for user interaction.
     * </p>
     * 
     * @param console the console manager instance for output operations, must not be null
     * @throws IllegalArgumentException if console is null
     */
    void setConsole(ConsoleManager console);
    
    /**
     * Sets the client application instance for handlers that need client access.
     * <p>
     * This method provides access to the client application for handlers that
     * need to perform client control operations (e.g., shutdown, connection management).
     * Default implementation does nothing as most handlers don't need client access.
     * </p>
     * 
     * @param client the client application instance, may be null
     */
    default void setClient(ClientApplication client) {}
}
