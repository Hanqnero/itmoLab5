package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.collection.MusicBand;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.GetByDate;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.GetByResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.util.CreationDateComparator;
import ru.hanqnero.uni.lab5.util.exceptions.WrongExecutorForCommandException;

import java.util.Comparator;

public class GetByExecutor implements CommandExecutor {
    CollectionManager collection;
    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof GetByDate get))
            throw new WrongExecutorForCommandException(command, this);

        Comparator<MusicBand> comparator;
        if (get.var() == GetByDate.Variant.MIN) {
            if (get.type() == GetByDate.DateType.CREATION) {
                comparator = new CreationDateComparator();
            } else {
                comparator = Comparator.naturalOrder();
            }
        } else {
            if (get.type() == GetByDate.DateType.CREATION) {
                comparator = new CreationDateComparator().reversed();
            } else {
                comparator = Comparator.reverseOrder();
            }
        }

        var element = collection.min(comparator);

        return element
                .map(musicBand -> new GetByResult(ExecutionResult.Status.SUCCESS, musicBand.toString()))
                .orElseGet(() -> new GetByResult(ExecutionResult.Status.ERROR, "No such element"));

    }

    @Override
    public void setCollection(CollectionManager collection) {
        this.collection = collection;
    }
}
