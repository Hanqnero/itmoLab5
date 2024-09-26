package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.ScriptCommand;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.ScriptResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.lab5.server.executors.AbstractCommandExecutor;

public class ScriptExecutor extends AbstractCommandExecutor {

    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof ScriptCommand scriptCommand))
            throw new WrongExecutorForCommandException(command, this);

        return new ScriptResult(ExecutionResult.Status.SUCCESS, scriptCommand.filename(), scriptCommand.isScriptEnd());
    }
}
