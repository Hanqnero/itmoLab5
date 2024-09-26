package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBand;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.RemoveGreaterCommand;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.RemoveGreaterResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.AbstractCommandExecutor;

public class RemoveGreaterExecutor extends AbstractCommandExecutor {
    public RemoveGreaterExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }
    @Override
    public ExecutionResult execute(Command command) {
        if(!(command instanceof RemoveGreaterCommand remove))
            throw new WrongExecutorForCommandException(command, this);

        var band = new MusicBand(remove.builder());

        long removed = getCollection().removeIf(e -> band.compareTo(e) > 0);
        return new RemoveGreaterResult(ExecutionResult.Status.SUCCESS, removed);
    }
}
