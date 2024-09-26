package ru.hanqnero.uni.lab5.server.executors;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;

public abstract class AbstractCommandExecutor implements CommandExecutor {
    private final CollectionManager collection;

    public AbstractCommandExecutor(CollectionManager collection) {
        this.collection = collection;
    }

    public AbstractCommandExecutor() {
        collection = null;
    }

    @Override
    public abstract ExecutionResult execute(Command command);

    public CollectionManager getCollection() {
        return collection;
    }
}
