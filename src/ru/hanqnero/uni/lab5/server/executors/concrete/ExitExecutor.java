package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.ExitResult;
import ru.hanqnero.uni.lab5.server.executors.AbstractCommandExecutor;

public class ExitExecutor extends AbstractCommandExecutor {
    @Override
    public ExecutionResult execute(Command command) {
        return new ExitResult(ExecutionResult.Status.SUCCESS);
    }
}
