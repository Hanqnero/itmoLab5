package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.ExitResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

public class ExitExecutor implements CommandExecutor {
    @Override
    public ExecutionResult execute(Command command) {
        return new ExitResult(ExecutionResult.Status.SUCCESS);
    }

    @Override
    public void setCollection(CollectionManager collection) {
        // Does nothing by design.
    }
}
