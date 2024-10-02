package ru.hanqnero.uni.secondsemester.server.executors.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.HelpCommand;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.HelpResult;
import ru.hanqnero.uni.secondsemester.server.exceptions.WrongExecutorForCommandException;
import ru.hanqnero.uni.secondsemester.server.executors.AbstractCommandExecutor;

import static ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult.Status.SUCCESS;

public class HelpExecutor extends AbstractCommandExecutor {
    @Override
    public HelpResult execute(Command command) {
        if (!(command instanceof HelpCommand)) {
            throw new WrongExecutorForCommandException(command, this);
        }
        return new HelpResult(SUCCESS);
    }
}
