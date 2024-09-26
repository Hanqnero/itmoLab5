package ru.hanqnero.uni.lab5.server.executors;

import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.ServerApplication;

public interface CommandExecutor {
    ExecutionResult execute(Command command);
    void setCollection(CollectionManager collection);
    default void setServer(ServerApplication server) {
        // Does nothing.
    }
}
