package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.ClearResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

public class ClearExecutor implements CommandExecutor {
    CollectionManager collection;

    @Override
    public ExecutionResult execute(Command command) {
        long n = collection.size();
        collection.clear();
        return new ClearResult(ExecutionResult.Status.SUCCESS, n);
    }

    @Override
    public void setCollection(CollectionManager collection) {
        this.collection = collection;
    }
}
