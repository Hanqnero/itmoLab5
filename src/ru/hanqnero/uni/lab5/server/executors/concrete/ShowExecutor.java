package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBand;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.results.concrete.ShowResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

import java.util.ArrayList;
import java.util.List;

public class ShowExecutor implements CommandExecutor {
    private CollectionManager collectionManager;
    @Override
    public ExecutionResult execute(Command command) {
        if (collectionManager.getCollectionCopy().isEmpty())
            return new ShowResult(ExecutionResult.Status.WARNING, new ArrayList<>());

        List<String> strings = collectionManager
                .getCollectionCopy()
                .stream()
                .map(MusicBand::toString )
                .toList();
        return new ShowResult(ExecutionResult.Status.SUCCESS, strings);
    }

    @Override
    public void setCollection(CollectionManager collection) {
        this.collectionManager = collection;
    }
}
