package ru.hanqnero.uni.lab5.client.factories;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

public interface CommandFactory {
    Command createCommand(String[] tokens) throws SubtypeScanError;
    default void setConsole(ConsoleManager consoleManager) {
        //Does nothing by default as not every command wil use ConsoleManager
    }
}
