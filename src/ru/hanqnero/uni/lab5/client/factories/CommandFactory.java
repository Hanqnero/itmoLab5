package ru.hanqnero.uni.lab5.client.factories;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.util.exceptions.SubtypeScanError;

public interface CommandFactory {
    Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError;
    default void setConsole(ConsoleManager consoleManager) {
        //Does nothing by default as not every command wil use ConsoleManager
    }
}
