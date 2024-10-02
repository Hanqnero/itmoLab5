package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.RemoveCommand;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.RemoveStudio;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.RemoveResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.UpdateResult;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;
import ru.hanqnero.uni.secondsemester.server.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

public class RemoveExecutor extends AbstractCommandExecutor {
    public RemoveExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public ExecutionResult execute(Command command) {
        long status = -1L;
        if (command instanceof RemoveCommand remove) {
            status = getCollection().remove(remove.id());
        }
        if (command instanceof RemoveStudio remove) {
            status = getCollection().remove(remove.studio());
        }
        if (status == -1L) {
            throw new WrongExecutorForCommandException(command, this);
        }
        if (status == 0) {
            return new RemoveResult(ExecutionResult.Status.WARNING, 0L);
        }
        return new UpdateResult(ExecutionResult.Status.SUCCESS, status);
    }
}
