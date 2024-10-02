package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.ScriptCommand;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.ScriptResult;
import ru.hanqnero.uni.secondsemester.server.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

public class ScriptExecutor extends AbstractCommandExecutor {

    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof ScriptCommand scriptCommand))
            throw new WrongExecutorForCommandException(command, this);

        return new ScriptResult(ExecutionResult.Status.SUCCESS, scriptCommand.filename(), scriptCommand.isScriptEnd());
    }
}
