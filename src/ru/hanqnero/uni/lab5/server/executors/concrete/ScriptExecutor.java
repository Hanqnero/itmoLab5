package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.client.ScriptCommand;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.ScriptResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.util.exceptions.WrongExecutorForCommandException;

public class ScriptExecutor implements CommandExecutor {

    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof ScriptCommand scriptCommand))
            throw new WrongExecutorForCommandException(command, this);

        return new ScriptResult(ExecutionResult.Status.SUCCESS, scriptCommand.filename(), scriptCommand.isScriptEnd());
    }

    @Override
    public void setCollection(CollectionManager collection) {

    }
}
