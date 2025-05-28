package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.collection.MusicBand;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.RemoveGreater;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.RemoveGreaterResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.util.exceptions.WrongExecutorForCommandException;

public class RemoveGreaterExecutor implements CommandExecutor {
    private CollectionManager collection;

    @Override
    public ExecutionResult execute(Command command) {
        if(!(command instanceof RemoveGreater remove))
            throw new WrongExecutorForCommandException(command, this);

        var band = new MusicBand(remove.builder());

        long removed = collection.removeIf(e -> band.compareTo(e) > 0);
        return new RemoveGreaterResult(ExecutionResult.Status.SUCCESS, removed);
    }

    @Override
    public void setCollection(CollectionManager collection) {
        this.collection = collection;
    }
}
