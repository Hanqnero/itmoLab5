package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.ClearResult;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

public class ClearExecutor extends AbstractCommandExecutor {
    public ClearExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public ExecutionResult execute(Command command) {
        long n = getCollection().size();
        getCollection().clear();
        return new ClearResult(ExecutionResult.Status.SUCCESS, n);
    }
}
