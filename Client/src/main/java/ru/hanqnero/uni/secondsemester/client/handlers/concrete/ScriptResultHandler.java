package ru.hanqnero.uni.secondsemester.client.handlers.concrete;

import ru.hanqnero.uni.secondsemester.client.ClientApplication;
import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.ScriptCommand;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.ScriptResult;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;
import ru.hanqnero.uni.secondsemester.commons.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.secondsemester.client.exceptions.SubtypeScanError;
import ru.hanqnero.uni.secondsemester.client.exceptions.WrongHandlerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class ScriptResultHandler extends AbstractExecutionResultHandler {
    private final ClientApplication client;
    public ScriptResultHandler(ConsoleManager console, ClientApplication client) {
        super(console);
        this.client = client;
    }
    public ClientApplication getClient() {
        return client;
    }

    @Override
    public void handleResult(ExecutionResult result) {
        //Read script and append command queue
        //abort on any error inside script

        if (!(result instanceof ScriptResult scriptResult))
            throw new WrongHandlerException(this, result);

        if (scriptResult.isScriptEnd()) {
            if (!(getConsole().getScriptManager().removeScriptFromStack(scriptResult.file()))) {
                getConsole().printlnErr("Script execution stack is in wrong order.");
            }
            return;
        } else {
            getConsole().getScriptManager().addScriptToStack(scriptResult.file());
        }

        Deque<Command> commands = new ArrayDeque<>();

        FileInputStream scriptInputStream;
        try {
            scriptInputStream = new FileInputStream(scriptResult.file());
        } catch (FileNotFoundException e) {
            getConsole().printlnErr("Script file not found: " + scriptResult.file());
            return;
        }

        var scriptConsole = new ConsoleManager(scriptInputStream, getConsole().getStdout());
        scriptConsole.setInteractiveMode(false);

        boolean errorInScript = false;

        try {
            while (scriptConsole.hasNext()) {
                String line = scriptConsole.nextLine();
                if (line.isBlank()) {
                    continue;
                }
                var tokens = line.split(" ", 1);
                assert tokens.length > 0;
                Optional<Command> command = getClient().createCommandFromTokens(tokens, scriptConsole);
                if (command.isEmpty()) {
                    errorInScript = true;
                    break;
                }

                if (command.get() instanceof ScriptCommand) {
                    File otherScript = ((ScriptCommand) command.get()).filename();
                    if (getConsole().getScriptManager().checkScriptRecursion(otherScript)) {
                        getConsole().printlnErr("Found script recursion");
                        errorInScript = true;

                    }
                }
                commands.push(command.get());
            }

            if (errorInScript) {
                getConsole().printlnErr("Didn't execute script as it contains errors");
                commands.clear();
            }
        } catch (ConsoleEmptyException e) {
            getConsole().printlnErr("Script ended unexpectedly");
        } catch (SubtypeScanError e) {
            getConsole().printlnErr("Error in script in MusicBand subtype");
        } catch (CommandCreationError e) {
            getConsole().printlnErr("Error in script in command invocation");
        }
        commands.push(new ScriptCommand(scriptResult.file(), true));
        getClient().addCommandsToQueue(commands);
    }
}
