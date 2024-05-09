package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.InfoCommand;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.InfoResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.util.exceptions.WrongExecutorForCommandException;

import java.time.LocalDateTime;

public class InfoExecutor implements CommandExecutor {
    private CollectionManager collection;
    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof InfoCommand info)) {
            throw new WrongExecutorForCommandException(command, this);
        }
        long collectionSize = collection.size();
        LocalDateTime creationTime = collection.getCreationTime();

        return new InfoResult(ExecutionResult.Status.SUCCESS, collectionSize, creationTime);
    }

    @Override
    public void setCollection(CollectionManager collection) {
        this.collection = collection;
    }

}
