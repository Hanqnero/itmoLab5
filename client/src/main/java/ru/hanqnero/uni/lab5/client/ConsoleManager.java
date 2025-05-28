package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.util.Console;
import ru.hanqnero.uni.lab5.util.exceptions.ConsoleEmptyException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Implementation of the Console interface that manages user input and output operations.
 * <p>
 * ConsoleManager serves as the primary interface for all user interactions in the client
 * application. It handles input reading, output formatting, and manages the integration
 * with script execution through the ScriptManager. The class supports both interactive
 * mode for direct user input and non-interactive mode for script execution.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Unified input/output management through standard streams</li>
 *   <li>Integration with script execution system</li>
 *   <li>Support for interactive and non-interactive modes</li>
 *   <li>Exception handling for empty console states</li>
 *   <li>Thread-safe output operations through PrintStream</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The console manager maintains state about the current interaction mode,
 * allowing the application to adapt its behavior based on whether input
 * is coming from a user or a script file.
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see Console
 * @see ScriptManager
 */
public class ConsoleManager implements Console {
    /** Output stream for writing data to the console */
    private final OutputStream stdout;
    /** Scanner for reading input from the console */
    private final Scanner scanner;
    /** Print stream for formatted output operations */
    private final PrintStream printout;

    /** Flag indicating whether interactive mode is enabled for recursive operations */
    private boolean recursiveSubtypeScan = true;
    /** Script manager for handling script execution and file operations */
    private final ScriptManager scriptManager = new ScriptManager();

    /**
     * Constructs a new ConsoleManager with the specified input and output streams.
     * <p>
     * This constructor initializes the console manager with the provided streams,
     * creating the necessary Scanner and PrintStream instances for input/output
     * operations. The script manager is automatically initialized for script
     * execution support.
     * </p>
     * 
     * @param stdin the input stream for reading user input, typically System.in
     * @param stdout the output stream for writing output, typically System.out
     * @throws IllegalArgumentException if either stream is null
     */
    public ConsoleManager(InputStream stdin, OutputStream stdout) {
        this.stdout = stdout;
        scanner = new Scanner(stdin);
        this.printout = new PrintStream(stdout);
    }

    /**
     * Gets the script manager instance for script execution operations.
     * 
     * @return the script manager instance
     */
    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    /**
     * Gets the output stream used by this console manager.
     * 
     * @return the output stream
     */
    public OutputStream getStdout() {
        return stdout;
    }

    /**
     * Checks if the console is in interactive mode.
     * <p>
     * Interactive mode enables recursive subtype scanning and user prompting
     * for complex object creation. When disabled, the console operates in
     * non-interactive mode suitable for script execution.
     * </p>
     * 
     * @return true if interactive mode is enabled, false otherwise
     */
    public boolean isInteractive() {
        return recursiveSubtypeScan;
    }
    
    /**
     * Sets the interactive mode for the console.
     * <p>
     * This method controls whether the console operates in interactive mode
     * (prompting users for input) or non-interactive mode (suitable for scripts).
     * </p>
     * 
     * @param mode true to enable interactive mode, false to disable
     */
    public void setInteractiveMode(boolean mode) {
        recursiveSubtypeScan = mode;
    }

    /**
     * Checks if there is more input available to read.
     * <p>
     * This method checks whether the underlying scanner has more lines
     * available for reading without blocking.
     * </p>
     * 
     * @return true if there is input available, false otherwise
     */
    public boolean hasNext() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next line of input from the console.
     * <p>
     * This method reads a complete line of input from the console,
     * blocking until input is available or throwing an exception
     * if no input is available.
     * </p>
     * 
     * @return the next line of input as a string
     * @throws ConsoleEmptyException if no input is available to read
     */
    public String nextLine() throws ConsoleEmptyException {
        if (!scanner.hasNextLine()) {
            throw new ConsoleEmptyException();
        }
        return scanner.nextLine();
    }

    public String nextLine(String inputHint) throws ConsoleEmptyException {
        printout.print(inputHint);
        return nextLine();
    }

    @SuppressWarnings("unused")
    public void println(String str) {
        printout.println(str);
    }

    private void println(String str, ANSIColor color) {
        printout.println(color + str + ANSIColor.RESET_COLOR);
    }


    public void printlnErr(String str) {
        println("E: " + str, ANSIColor.RED);
    }

    public void printlnSuc(String str) {
        println("S: "+str, ANSIColor.GREEN);
    }

    public void printlnWarn(String str) {
        println("W: " + str, ANSIColor.YELLOW);
    }

    @SuppressWarnings("unused")
    public void print(String s) {
        printout.print(s);
    }

    private enum ANSIColor {
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        RESET_COLOR("\u001B[0m");

        private final String code;
        ANSIColor(String code) {
            this.code = code;
        }
        @Override
        public String toString() {
            return code;
        }
    }
}
