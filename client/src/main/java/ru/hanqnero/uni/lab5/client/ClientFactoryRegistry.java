package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.client.factories.RemoveGreaterFactory;
import ru.hanqnero.uni.lab5.client.factories.concrete.*;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.client.handlers.concrete.*;
import ru.hanqnero.uni.lab5.contract.CommandInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for managing command factories and result handlers on the client side.
 * <p>
 * ClientFactoryRegistry serves as a central factory and registry for both command
 * factories (responsible for creating commands from user input) and execution result
 * handlers (responsible for processing and displaying command results). This class
 * implements the Registry pattern to provide organized access to client-side
 * command processing components.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Centralized factory and handler management</li>
 *   <li>Static factory methods for creating component mappings</li>
 *   <li>Type-safe mapping between command names and processors</li>
 *   <li>Separation of command creation and result handling concerns</li>
 *   <li>Easy extension with new command types</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The registry provides two main mappings:
 * <ul>
 *   <li>Command factories - for creating Command objects from user input</li>
 *   <li>Result handlers - for processing ExecutionResult objects from the server</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see CommandFactory
 * @see ExecutionResultHandler
 * @see CommandInfo
 */
public class ClientFactoryRegistry {
    
    /**
     * Creates and returns a complete mapping of command names to their factory instances.
     * <p>
     * This factory method instantiates all available command factories and creates
     * a HashMap mapping from command names to their corresponding factory implementations.
     * The factories are responsible for parsing user input and creating properly
     * initialized Command objects for transmission to the server.
     * </p>
     * 
     * @return a new HashMap containing mappings from command names to factory instances
     * @throws RuntimeException if factory instantiation fails
     */
    public static Map<String, CommandFactory> createFactoriesView() {
        Map<String, CommandFactory> factories = new HashMap<>();
        factories.put(CommandInfo.HELP.getName(), new HelpFactory());
        factories.put(CommandInfo.EXIT.getName(), new ExitFactory());
        factories.put(CommandInfo.INFO.getName(), new InfoFactory());
        factories.put(CommandInfo.SHOW.getName(), new ShowFactory());
        factories.put(CommandInfo.ADD.getName(), new AddFactory());
        factories.put(CommandInfo.UPDATE.getName(), new UpdateFactory());
        factories.put(CommandInfo.REMOVE.getName(), new RemoveFactory());
        factories.put(CommandInfo.CLEAR.getName(), new ClearFactory());
        factories.put(CommandInfo.SCRIPT.getName(), new ScriptFactory());
        factories.put(CommandInfo.SAVE.getName(), new SaveFactory());
        factories.put(CommandInfo.REMOVE_GREATER.getName(), new RemoveGreaterFactory());
        factories.put(CommandInfo.GET_BY.getName(), new GetByFactory());
        return factories;
    }

    /**
     * Creates and returns a complete mapping of command names to their result handler instances.
     * <p>
     * This factory method instantiates all available execution result handlers and creates
     * a HashMap mapping from command names to their corresponding handler implementations.
     * The handlers are responsible for processing ExecutionResult objects received from
     * the server and presenting the results to the user in an appropriate format.
     * </p>
     * 
     * <p>
     * Each handler is specialized for processing the specific type of result that
     * corresponds to its command type, ensuring proper formatting, error handling,
     * and user interaction for each command's response.
     * </p>
     * 
     * @return a new HashMap containing mappings from command names to handler instances
     * @throws RuntimeException if handler instantiation fails
     */
    public static Map<String, ExecutionResultHandler> createHandlersView() {
        Map<String, ExecutionResultHandler> handlers = new HashMap<>();
        handlers.put(CommandInfo.HELP.getName(), new HelpResultHandler());
        handlers.put(CommandInfo.EXIT.getName(), new ExitResultHandler());
        handlers.put(CommandInfo.INFO.getName(), new InfoHandler());
        handlers.put(CommandInfo.SHOW.getName(), new ShowHandler());
        handlers.put(CommandInfo.ADD.getName(), new AddResultHandler());
        handlers.put(CommandInfo.UPDATE.getName(), new UpdateHandler());
        handlers.put(CommandInfo.REMOVE.getName(), new RemoveHandler());
        handlers.put(CommandInfo.CLEAR.getName(), new ClearHandler());
        handlers.put(CommandInfo.SCRIPT.getName(), new ScriptResultHandler());
        handlers.put(CommandInfo.SAVE.getName(), new SaveHandler());
        handlers.put(CommandInfo.REMOVE_GREATER.getName(), new RemoveGreaterHandler());
        handlers.put(CommandInfo.GET_BY.getName(), new GetByHandler());
        return handlers;
    }
}
