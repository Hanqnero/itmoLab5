package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.collection.MusicBand;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.UpdateCommand;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.UpdateResult;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;
import ru.hanqnero.uni.secondsemester.server.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

public class UpdateExecutor extends AbstractCommandExecutor {
    public UpdateExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof UpdateCommand update))
            throw new WrongExecutorForCommandException(command, this);

        var success = getCollection().update(update.id(), new MusicBand(update.builder()));
        if (success.isPresent()) {
            if (success.get()) {
                return new UpdateResult(ExecutionResult.Status.SUCCESS, update.id());
            } else {
                return new UpdateResult(ExecutionResult.Status.WARNING, update.id());
            }
        } else {
            return new UpdateResult(ExecutionResult.Status.ERROR, update.id());
        }
    }
}
