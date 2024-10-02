package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.collection.MusicBand;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.AddCommand;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.AddResult;
import ru.hanqnero.uni.secondsemester.server.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

import java.util.Optional;

public class AddExecutor extends AbstractCommandExecutor {
    public AddExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof AddCommand add)) {
            throw new WrongExecutorForCommandException(command, this);
        }
        var newBand = new MusicBand(add.builder());

        switch (add.variant()) {
            case MIN -> {
                if (!getCollection().isMin(newBand))
                    return new AddResult(ExecutionResult.Status.ERROR, 0L);
            }
            case MAX -> {
                if (!getCollection().isMax(newBand))
                    return new AddResult(ExecutionResult.Status.ERROR, 0L);
            }
            default -> {}
        }
            Optional<Long> newId = getCollection().add(newBand);
            return newId.map(
                            aLong -> new AddResult(ExecutionResult.Status.SUCCESS, aLong))
                    .orElseGet(() -> new AddResult(ExecutionResult.Status.ERROR, 0L));
    }
}
