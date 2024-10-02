package ru.hanqnero.uni.secondsemester.client.factories;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;
import ru.hanqnero.uni.secondsemester.client.exceptions.SubtypeScanError;

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
