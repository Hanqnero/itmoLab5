package ru.hanqnero.uni.secondsemester.client.factories;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;
import ru.hanqnero.uni.secondsemester.client.exceptions.SubtypeScanError;

public interface CommandFactory {
    Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError;
    default void setConsole(ConsoleManager consoleManager) {
        //Does nothing by default as not every command wil use ConsoleManager
    }
}
