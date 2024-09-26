package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBand;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.GetByDateCommand;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.GetByResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.executors.AbstractCommandExecutor;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.commons.util.CreationDateComparator;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongExecutorForCommandException;

import java.util.Comparator;

public class GetByDateExecutor extends AbstractCommandExecutor {
    public GetByDateExecutor(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public ExecutionResult execute(Command command) {
        if (!(command instanceof GetByDateCommand get))
            throw new WrongExecutorForCommandException(command, this);

        Comparator<MusicBand> comparator;
        if (get.var() == GetByDateCommand.Variant.MIN) {
            if (get.type() == GetByDateCommand.DateType.CREATION) {
                comparator = new CreationDateComparator();
            } else {
                comparator = Comparator.naturalOrder();
            }
        } else {
            if (get.type() == GetByDateCommand.DateType.CREATION) {
                comparator = new CreationDateComparator().reversed();
            } else {
                comparator = Comparator.reverseOrder();
            }
        }

        var element = getCollection().min(comparator);

        return element
                .map(musicBand -> new GetByResult(ExecutionResult.Status.SUCCESS, musicBand.toString()))
                .orElseGet(() -> new GetByResult(ExecutionResult.Status.ERROR, "No such element"));

    }
}
