package ru.hanqnero.uni.lab5.contract.results;

import java.io.Serializable;

/**
 * Interface representing the result of command execution on the server.
 * 
 * <p>ExecutionResult objects are sent back from the server to the client
 * after a command has been processed. They contain the execution status
 * and are associated with the original command that was executed.</p>
 * 
 * <p>Results must be serializable to enable network transmission back
 * to the client via TCP. The status indicates whether the command
 * executed successfully, encountered an error, or completed with warnings.</p>
 * 
 * @author hanqnero
 * @version 1.0
 */
public interface ExecutionResult extends Serializable {
    /**
     * Enumeration of possible execution statuses.
     */
    enum Status {
        /** Command executed successfully */
        SUCCESS, 
        /** Command execution failed with an error */
        ERROR, 
        /** Command executed but with warnings */
        WARNING
    }

    /**
     * Gets the execution status of the command.
     * 
     * @return the status indicating success, error, or warning
     */
    Status getStatus();
    
    /**
     * Gets the name of the command that was executed.
     * 
     * @return the command name as a string
     */
    String getCommandName();

}
