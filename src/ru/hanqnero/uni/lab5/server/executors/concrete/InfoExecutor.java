package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.results.concrete.InfoResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

import java.time.LocalDateTime;

public class InfoExecutor implements CommandExecutor {
    private CollectionManager collection;
    @Override
    public ExecutionResult execute(Command command) {

        long collectionSize = collection.size();
        LocalDateTime creationTime = collection.getCreationTime();

        return new InfoResult(ExecutionResult.Status.SUCCESS, collectionSize, creationTime);
    }

    @Override
    public void setCollection(CollectionManager collection) {
        this.collection = collection;
    }

}
