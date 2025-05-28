package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ClientApplication;
import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.ScriptCommand;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.ScriptResult;
import ru.hanqnero.uni.lab5.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.util.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;
import ru.hanqnero.uni.lab5.util.exceptions.WrongHandlerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class ScriptResultHandler implements ExecutionResultHandler {
    private ClientApplication client;
    private ConsoleManager console;

    @Override
    public void handleResult(ExecutionResult result) {
        //Read script and append command queue
        //abort on any error inside script

        if (!(result instanceof ScriptResult scriptResult))
            throw new WrongHandlerException(this, result);

        if (scriptResult.isScriptEnd()) {
            if (!(console.getScriptManager().removeScriptFromStack(scriptResult.file()))) {
                console.printlnErr("Script execution stack is in wrong order.");
            }
            return;
        } else {
            console.getScriptManager().addScriptToStack(scriptResult.file());
        }

        Deque<Command> commands = new ArrayDeque<>();

        FileInputStream scriptInputStream;
        try {
            scriptInputStream = new FileInputStream(scriptResult.file());
        } catch (FileNotFoundException e) {
            console.printlnErr("Script file not found: " + scriptResult.file());
            return;
        }

        var scriptConsole = new ConsoleManager(scriptInputStream, console.getStdout());
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
                Optional<Command> command = client.createCommandFromTokens(tokens, scriptConsole);
                if (command.isEmpty()) {
                    errorInScript = true;
                    break;
                }

                if (command.get() instanceof ScriptCommand) {
                    File otherScript = ((ScriptCommand) command.get()).filename();
                    if (console.getScriptManager().checkScriptRecursion(otherScript)) {
                        console.printlnErr("Found script recursion");
                        errorInScript = true;

                    }
                }
                commands.push(command.get());
            }

            if (errorInScript) {
                console.printlnErr("Didn't execute script as it contains errors");
                commands.clear();
            }
        } catch (ConsoleEmptyException e) {
            console.printlnErr("Script ended unexpectedly");
        } catch (SubtypeScanError e) {
            console.printlnErr("Error in script in MusicBand subtype");
        } catch (CommandCreationError e) {
            console.printlnErr("Error in script in command invocation");
        }
        commands.push(new ScriptCommand(scriptResult.file(), true));
        client.addCommandsToQueue(commands);
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }

    @Override
    public void setClient(ClientApplication client) {
        this.client = client;
    }
}
