package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.Exit;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;

public class ExitFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) {
        return new Exit();
    }

    @Override
    public void setConsole(ConsoleManager consoleManager) {
        // Does nothing.
    }
}
