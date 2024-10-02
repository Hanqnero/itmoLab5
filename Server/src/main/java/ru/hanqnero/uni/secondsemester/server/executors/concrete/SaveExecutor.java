package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.SaveResult;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

public class SaveExecutor extends AbstractCommandExecutor {
    public SaveExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }
    @Override
    public ExecutionResult execute(Command command) {
        long size = getCollection().size();
        long savedSize = getCollection().saveToFile();
        return new SaveResult(ExecutionResult.Status.SUCCESS, size, savedSize);
    }
}
