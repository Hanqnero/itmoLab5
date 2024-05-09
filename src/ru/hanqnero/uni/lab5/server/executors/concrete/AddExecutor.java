package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.collection.MusicBand;
import ru.hanqnero.uni.lab5.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.AddCommand;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.AddResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.util.exceptions.WrongExecutorForCommandException;

import java.util.Optional;

public class AddExecutor implements CommandExecutor {
    private CollectionManager collectionManager;
    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof AddCommand add)) {
            throw new WrongExecutorForCommandException(command, this);
        }
        MusicBandBuilder builder = new MusicBandBuilder(
                add.name(),
                add.coordinates(),
                add.participants(),
                add.singles(),
                add.estDate()
        )
                .setStudio(add.studio())
                .setGenre(add.genre());
        Optional<Long> newId = collectionManager.add(new MusicBand(builder));
        return newId.map(
                aLong -> new AddResult(ExecutionResult.Status.SUCCESS, aLong))
                .orElseGet(() -> new AddResult(ExecutionResult.Status.ERROR, 0L));
    }

    @Override
    public void setCollection(CollectionManager collection) {
        collectionManager = collection;
    }
}
