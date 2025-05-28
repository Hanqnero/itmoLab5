package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.SaveResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

public class SaveExecutor implements CommandExecutor
{
    private CollectionManager collection;

    @Override
    public ExecutionResult execute(Command command) {
        long size = collection.size();
        long savedSize = collection.saveToFile();
        return new SaveResult(ExecutionResult.Status.SUCCESS, size, savedSize);
    }

    @Override
    public void setCollection(CollectionManager collection) {
        this.collection = collection;
    }
}
