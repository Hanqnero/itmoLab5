package ru.hanqnero.uni.lab5.client.factories;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

/**
 * Interface defining the contract for command creation on the client side.
 * <p>
 * CommandFactory implementations are responsible for parsing user input tokens
 * and creating appropriate Command objects. Each factory handles the creation
 * of specific command types and manages the collection of required parameters
 * from the user through console interaction.
 * </p>
 * 
 * <p>
 * Key responsibilities:
 * <ul>
 *   <li>Parse command tokens from user input</li>
 *   <li>Validate command syntax and arguments</li>
 *   <li>Collect additional parameters through console interaction</li>
 *   <li>Create properly initialized Command objects</li>
 *   <li>Handle parameter validation and error reporting</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Factory pattern benefits:
 * <ul>
 *   <li>Encapsulates command creation logic</li>
 *   <li>Provides consistent error handling across command types</li>
 *   <li>Allows for easy extension with new command types</li>
 *   <li>Separates user interaction from command business logic</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see Command
 * @see ConsoleManager
 */
public interface CommandFactory {
    /**
     * Creates a command from the provided input tokens.
     * <p>
     * This method parses the command tokens, validates the syntax and arguments,
     * and may interact with the user through the console manager to collect
     * additional required parameters. The method performs comprehensive validation
     * and provides detailed error messages for invalid input.
     * </p>
     * 
     * @param tokens array of command tokens where tokens[0] is the command name
     *               and subsequent tokens are arguments, must not be null or empty
     * @return a properly initialized Command object ready for execution
     * @throws SubtypeScanError if there are issues scanning for enum subtypes or
     *                          validating complex object parameters
     * @throws CommandCreationError if the command cannot be created due to invalid
     *                             arguments, missing parameters, or syntax errors
     * @throws IllegalArgumentException if tokens array is null or empty
     */
    Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError;
    
    /**
     * Sets the console manager for user interaction.
     * <p>
     * The console manager is used to prompt the user for additional parameters
     * when creating commands that require complex input (e.g., MusicBand objects).
     * Default implementation does nothing as not every command requires console interaction.
     * </p>
     * 
     * @param consoleManager the console manager instance for user interaction,
     *                       may be null for commands that don't require user input
     */
    default void setConsole(ConsoleManager consoleManager) {
        //Does nothing by default as not every command wil use ConsoleManager
    }
}
