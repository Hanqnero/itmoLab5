package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBand;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.UpdateCommand;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.UpdateResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongExecutorForCommandException;

public class UpdateExecutor implements CommandExecutor {
    private CollectionManager collection;

    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof UpdateCommand update))
            throw new WrongExecutorForCommandException(command, this);

        var success = collection.update(update.id(), new MusicBand(update.builder()));
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

    @Override
    public void setCollection(CollectionManager collection) {
        this.collection = collection;
    }
}
