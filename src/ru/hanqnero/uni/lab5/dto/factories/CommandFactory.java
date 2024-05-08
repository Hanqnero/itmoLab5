package ru.hanqnero.uni.lab5.dto.factories;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.dto.commands.Command;

public interface CommandFactory {
    Command createCommand(String[] tokens);
    CommandFactory setConsoleManager(ConsoleManager consoleManager);
}
