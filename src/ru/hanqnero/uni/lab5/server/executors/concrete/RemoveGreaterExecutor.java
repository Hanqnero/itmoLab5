package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBand;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.commands.concrete.RemoveGreater;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.results.concrete.RemoveGreaterResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.commons.util.exceptions.WrongExecutorForCommandException;

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
