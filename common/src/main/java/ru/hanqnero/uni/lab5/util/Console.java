package ru.hanqnero.uni.lab5.util;

import ru.hanqnero.uni.lab5.util.exceptions.ConsoleEmptyException;

import java.io.OutputStream;

/**
 * Interface defining the contract for console input and output operations.
 * <p>
 * The Console interface provides a unified abstraction for user interaction,
 * supporting both interactive console sessions and non-interactive script
 * execution. It handles input reading, output formatting, and mode switching
 * between interactive and batch processing modes.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Interactive and non-interactive mode support</li>
 *   <li>Prompted and non-prompted input reading</li>
 *   <li>Output stream access for direct writing</li>
 *   <li>Input availability checking</li>
 *   <li>Exception handling for empty console states</li>
 * </ul>
 * </p>
 * 
 * <p>
 * This interface enables consistent user interaction across different
 * execution contexts (direct user input vs. script execution) while
 * providing the flexibility needed for various input/output scenarios.
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see ConsoleEmptyException
 */
public interface Console {
    /**
     * Reads the next line of input with a prompt.
     * 
     * @param prompt the prompt to display to the user
     * @return the next line of input
     * @throws ConsoleEmptyException if no input is available
     */
    String nextLine(String prompt) throws ConsoleEmptyException;
    
    /**
     * Reads the next line of input without a prompt.
     * 
     * @return the next line of input
     * @throws ConsoleEmptyException if no input is available
     */
    String nextLine() throws ConsoleEmptyException;
    
    /**
     * Checks if more input is available.
     * 
     * @return true if input is available, false otherwise
     */
    boolean hasNext();
    
    /**
     * Checks if the console is in interactive mode.
     * 
     * @return true if interactive mode is enabled, false otherwise
     */
    boolean isInteractive();
    
    /**
     * Prints a line to the console output.
     * 
     * @param line the line to print
     */
    void println(String line);
    
    /**
     * Gets the output stream for direct writing.
     * 
     * @return the output stream
     */
    OutputStream getStdout();
    
    /**
     * Sets the interactive mode for the console.
     * 
     * @param interactive true to enable interactive mode, false for batch mode
     */
    void setInteractiveMode(boolean interactive);
}
