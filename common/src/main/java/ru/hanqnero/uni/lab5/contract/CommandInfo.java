package ru.hanqnero.uni.lab5.contract;

import java.util.*;

/**
 * Enumeration defining all available commands in the music band collection system.
 * <p>
 * CommandInfo serves as a central registry of all supported commands, providing
 * both the command names used for parsing and the descriptions displayed to users.
 * This enum ensures consistency between client command parsing and server command
 * execution by providing a single source of truth for command definitions.
 * </p>
 * 
 * <p>
 * Each command is defined with:
 * <ul>
 *   <li>A unique name used for command identification and parsing</li>
 *   <li>A description explaining the command's purpose and syntax</li>
 *   <li>Implicit documentation of expected parameters and behavior</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The enum supports both simple commands (like "help", "info") and complex
 * commands with parameters (like "add", "update", "remove"). Parameter syntax
 * is documented in the description field using standard conventions.
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 */
public enum CommandInfo {
    /** Display help information about available commands */
    HELP("help", "- Display list of commands"),
    /** Exit the client application without saving changes */
    EXIT("exit", "- Stop client without saving any changes"),
    /** Display information about the collection (size, type, initialization date) */
    INFO("info", "- Display information about collection"),
    /** Display all elements in the collection */
    SHOW("show", "- Display list of every collection item"),
    /** Add a new music band to the collection with optional min/max criteria */
    ADD("add", "--[min|max] {Music Band} - Add element to collection"),
    /** Update an existing music band by its ID */
    UPDATE("update", "<id> {Music Band} - Update element in collection"),
    /** Remove music band(s) by ID or studio criteria */
    REMOVE("remove", "--[id <id>|--studio {Studio}] - Remove element with matching id from collection"),
    /** Clear all elements from the collection */
    CLEAR("clear", "- Remove all items from the collection"),
    /** Execute a sequence of commands from a script file */
    SCRIPT("execute", "<filename> - Execute list of commands from file"),
    /** Save the collection to persistent storage */
    SAVE("save", "- Save collection to text file"),
    /** Remove all bands with more participants than the specified band */
    REMOVE_GREATER("remgr", "{Music Band} - Remove all elements exceeding this from collection"),
    /** Get the first or last element based on sorting criteria */
    GET_BY("get", "--[min|max] --[creation|establishment]- Display first or last element after chosen sorting");

    /** The command name used for parsing and identification */
    private final String name;
    /** The human-readable description of the command and its syntax */
    private final String description;

    /**
     * Constructs a CommandInfo enum constant with the specified name and description.
     * 
     * @param name the command name used for parsing and identification
     * @param description the human-readable description of the command
     */
    CommandInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the command name used for parsing and identification.
     * 
     * @return the command name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the human-readable description of the command and its syntax.
     * 
     * @return the command description
     */
    public String getDescription() {
        return description;
    }
}
