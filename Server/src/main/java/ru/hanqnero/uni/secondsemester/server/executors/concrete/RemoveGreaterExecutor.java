package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.collection.MusicBand;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.RemoveGreaterCommand;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.RemoveGreaterResult;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;
import ru.hanqnero.uni.secondsemester.server.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

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
