package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.client.ScriptCommand;
import ru.hanqnero.uni.lab5.util.exceptions.CommandCreationError;

import java.io.File;

public class ScriptFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) throws CommandCreationError {
        if (tokens.length != 2) {
            console.printlnErr("Incorrect number of arguments: " + tokens.length + " but expected 2");
            throw new CommandCreationError();
        }

        File scriptFile = new File(tokens[1]);
        if (!scriptFile.isFile()) {
            console.printlnErr("Script file is not a file: " + tokens[1]);
            throw new CommandCreationError();
        }

        return new ScriptCommand(scriptFile, false);
    }

    private ConsoleManager console;
    @Override
    public void setConsole(ConsoleManager consoleManager) {
        this.console = consoleManager;
    }

}
