package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBand;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.AddCommand;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.AddResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.AbstractCommandExecutor;

import java.util.Optional;

public class AddExecutor extends AbstractCommandExecutor {
    private CollectionManager collectionManager;

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
                if (!collectionManager.isMin(newBand))
                    return new AddResult(ExecutionResult.Status.ERROR, 0L);
            }
            case MAX -> {
                if (!collectionManager.isMax(newBand))
                    return new AddResult(ExecutionResult.Status.ERROR, 0L);
            }
            default -> {}
        }
            Optional<Long> newId = collectionManager.add(newBand);
            return newId.map(
                            aLong -> new AddResult(ExecutionResult.Status.SUCCESS, aLong))
                    .orElseGet(() -> new AddResult(ExecutionResult.Status.ERROR, 0L));
    }
}
