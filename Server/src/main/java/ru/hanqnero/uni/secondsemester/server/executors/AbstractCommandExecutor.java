package ru.hanqnero.uni.secondsemester.server.executors;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;

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
