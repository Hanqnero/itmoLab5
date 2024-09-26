package ru.hanqnero.uni.lab5.server.executors;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.ServerApplication;

public interface CommandExecutor {
    ExecutionResult execute(Command command);
}
