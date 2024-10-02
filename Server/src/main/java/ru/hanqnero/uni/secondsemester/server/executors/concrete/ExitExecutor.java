package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.ExitResult;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

public class ExitExecutor extends AbstractCommandExecutor {
    @Override
    public ExecutionResult execute(Command command) {
        return new ExitResult(ExecutionResult.Status.SUCCESS);
    }
}
