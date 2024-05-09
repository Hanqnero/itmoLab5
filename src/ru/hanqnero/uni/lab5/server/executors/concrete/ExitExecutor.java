package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

public class ExitExecutor implements CommandExecutor {

    @Override
    public ExecutionResult execute(Command command) {
        System.out.println("Stopping server...");
        System.exit(42);
        return null;
    }

    @Override
    public void setCollection(CollectionManager collection) {
        // Does nothing by design.
    }
}
