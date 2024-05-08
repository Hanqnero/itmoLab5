package ru.hanqnero.uni.lab5.dto.executors;

import ru.hanqnero.uni.lab5.dto.commands.Command;
import ru.hanqnero.uni.lab5.dto.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;

import java.util.Collection;

public interface CommandExecutor {
    ExecutionResult execute(Command command);
    void setCollection(CollectionManager collection);
    CollectionManager getCollection();
}
