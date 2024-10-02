package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.InfoResult;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

import java.time.LocalDateTime;

public class InfoExecutor extends AbstractCommandExecutor {
    public InfoExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }
    @Override
    public ExecutionResult execute(Command command) {

        long collectionSize = getCollection().size();
        LocalDateTime creationTime = getCollection().getCreationTime();

        return new InfoResult(ExecutionResult.Status.SUCCESS, collectionSize, creationTime);
    }
}
