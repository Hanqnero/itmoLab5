package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.dto.CommandInfo;
import ru.hanqnero.uni.lab5.dto.commands.Command;
import ru.hanqnero.uni.lab5.dto.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.dto.results.ExecutionResult;

import java.util.Map;

public class ServerApplication {
    private final Map<String, CommandExecutor> executors;

    public ServerApplication(CollectionManager collection) {
        executors = CommandInfo.createAllExecutorsView();
        executors.values().forEach(e -> e.setCollection(collection));
    }

    public ExecutionResult response(Command command) {
        return this.executors.get(command.getName()).execute(command);
    }
}
