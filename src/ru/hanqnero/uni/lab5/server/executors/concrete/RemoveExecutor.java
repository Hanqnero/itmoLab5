package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.RemoveId;
import ru.hanqnero.uni.lab5.contract.commands.concrete.RemoveStudio;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.RemoveResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.UpdateResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.util.exceptions.WrongExecutorForCommandException;

public class RemoveExecutor implements CommandExecutor {
    private CollectionManager collection;
    @Override
    public ExecutionResult execute(Command command) {
        long status = -1L;
        if (command instanceof RemoveId remove) {
            status = collection.remove(remove.id());
        }
        if (command instanceof RemoveStudio remove) {
            status = collection.remove(remove.studio());
        }
        if (status == -1L) {
            throw new WrongExecutorForCommandException(command, this);
        }
        if (status == 0) {
            return new RemoveResult(ExecutionResult.Status.WARNING, 0L);
        }
        return new UpdateResult(ExecutionResult.Status.SUCCESS, status);
    }

    @Override
    public void setCollection(CollectionManager collection) {
        this.collection = collection;
    }
}
