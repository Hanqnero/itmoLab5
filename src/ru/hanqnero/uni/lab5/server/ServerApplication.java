package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

import java.util.Map;

public class ServerApplication {
    private final CommandInfo[] availableCommandInfo;
    private final Map<String, CommandExecutor> executors;

    public ServerApplication(CollectionManager collection) {

        availableCommandInfo = CommandInfo.values().clone();

        executors = CommandInfo.createExecutorsView(availableCommandInfo);
        executors.values().forEach(e -> e.setCollection(collection));
    }

    public ExecutionResult response(Command command) {
        var executor = this.executors.get(command.getName());

        if (executor == null) {
            return null;
        }
        executor.setServer(this);
        return executor.execute(command);
    }

    public CommandInfo[] getAvailableCommandInfo() {
        return availableCommandInfo;
    }
}
