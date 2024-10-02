package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.collection.MusicBand;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.ShowResult;
import ru.hanqnero.uni.secondsemester.server.CollectionManager;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

import java.util.ArrayList;
import java.util.List;

public class ShowExecutor extends AbstractCommandExecutor {
    public ShowExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }
    @Override
    public ExecutionResult execute(Command command) {
        if (getCollection().getCollectionCopy().isEmpty())
            return new ShowResult(ExecutionResult.Status.WARNING, new ArrayList<>());

        List<String> strings = getCollection()
                .getCollectionCopy()
                .stream()
                .map(MusicBand::toString )
                .toList();
        return new ShowResult(ExecutionResult.Status.SUCCESS, strings);
    }
}
