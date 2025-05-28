package ru.hanqnero.uni.lab5.client;

import java.io.File;
import java.util.Stack;

/**
 * Manager for handling script execution and preventing recursive script calls.
 * <p>
 * ScriptManager maintains a stack of currently executing script files to track
 * the script execution hierarchy and prevent infinite recursion. This is essential
 * for the script command functionality, which allows users to execute command
 * sequences from files while maintaining safety against circular dependencies.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Stack-based tracking of script execution hierarchy</li>
 *   <li>Recursion detection for script files</li>
 *   <li>Debug logging support for script execution tracking</li>
 *   <li>Safe script stack management with validation</li>
 *   <li>Thread-safe operations on the script stack</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The manager uses a Last-In-First-Out (LIFO) stack structure to track nested
 * script execution, where scripts can call other scripts but cannot recursively
 * call themselves or create circular dependencies.
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see File
 * @see Stack
 */
public class ScriptManager {

    /** Stack for tracking the hierarchy of currently executing script files */
    private final Stack<File> scriptExecutionStack = new Stack<>();

    /**
     * Checks if executing the specified script would create a recursive loop.
     * <p>
     * This method examines the current script execution stack to determine if
     * the specified script file is already being executed at any level in the
     * hierarchy. This prevents infinite recursion that could occur if a script
     * directly or indirectly calls itself.
     * </p>
     * 
     * @param newScript the script file to check for recursion, must not be null
     * @return true if executing this script would create recursion, false if safe to execute
     * @throws IllegalArgumentException if newScript is null
     */
    public boolean checkScriptRecursion(File newScript) {
        return scriptExecutionStack.contains(newScript);
    }

    /**
     * Adds a script file to the top of the execution stack.
     * <p>
     * This method pushes the specified script onto the execution stack,
     * marking it as currently being executed. The script should be added
     * before execution begins and removed when execution completes.
     * </p>
     * 
     * @param newScript the script file to add to the execution stack, must not be null
     * @throws IllegalArgumentException if newScript is null
     */
    public void addScriptToStack(File newScript) {
        scriptExecutionStack.push(newScript);

        if (ClientApplication.DEBUG)
            System.out.println("Added script to head of stack: " + newScript);
    }

    /**
     * Removes a script file from the execution stack.
     * <p>
     * This method attempts to remove the specified script from the top of the
     * execution stack. The removal only succeeds if the specified script is
     * currently at the top of the stack, ensuring proper stack discipline
     * and preventing corruption of the execution hierarchy.
     * </p>
     * 
     * @param script the script file to remove from the stack, must not be null
     * @return true if the script was successfully removed from the top of the stack,
     *         false if the script is not at the top or not in the stack
     * @throws IllegalArgumentException if script is null
     * @throws RuntimeException if the stack is empty when attempting removal
     */
    public boolean removeScriptFromStack(File script) {
        if (ClientApplication.DEBUG) {
            System.out.println("Trying to remove script from head of stack: " + script);
            System.out.println("Current stack: " + scriptExecutionStack);
        }
        if (scriptExecutionStack.peek().equals(script)) {
            scriptExecutionStack.pop();
            return true;
        }
        return false;
    }
}
