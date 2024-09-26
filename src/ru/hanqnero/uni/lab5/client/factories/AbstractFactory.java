package ru.hanqnero.uni.lab5.client.factories;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.exceptions.SubtypeScanError;

public abstract class AbstractFactory implements CommandFactory {
    private final ConsoleManager console;

    public AbstractFactory(ConsoleManager console) {
        this.console = console;
    }

    public AbstractFactory() {
        this.console = null;
    }

    public ConsoleManager getConsole() {
        return console;
    }

    @Override
    abstract public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError;
}
