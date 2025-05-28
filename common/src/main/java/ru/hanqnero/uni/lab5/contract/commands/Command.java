package ru.hanqnero.uni.lab5.contract.commands;

import java.io.Serializable;

/**
 * Base interface for all client commands that can be executed on the server.
 * 
 * <p>This interface defines the contract for commands that can be sent from
 * the client to the server for execution. All commands must be serializable
 * to enable network transmission via TCP.</p>
 * 
 * <p>Commands encapsulate user requests such as adding music bands, querying
 * the collection, or performing administrative operations. Each command has
 * a unique name that identifies its type and purpose.</p>
 * 
 * @author hanqnero
 * @version 1.0
 */
public interface Command extends Serializable{
    /**
     * Gets the name of this command.
     * 
     * <p>The command name is used by the server to identify which executor
     * should handle this command. Names should be unique and descriptive.</p>
     * 
     * @return the command name as a string
     */
    String getName();
}