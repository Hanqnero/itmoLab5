package ru.hanqnero.uni.secondsemester.client.factories.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.factories.AbstractFactory;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.ScriptCommand;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;

import java.io.File;

public class ScriptFactory extends AbstractFactory {
    private final ConsoleManager console;

    public ScriptFactory(ConsoleManager console) {
        this.console = console;
    }

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
}
