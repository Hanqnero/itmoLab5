package ru.hanqnero.uni.secondsemester.client.handlers.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.RemoveResult;
import ru.hanqnero.uni.secondsemester.client.exceptions.WrongHandlerException;

public class RemoveResultHandler extends AbstractExecutionResultHandler {
    public RemoveResultHandler(ConsoleManager console) {
        super(console);
    }
    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof RemoveResult remove))
            throw new WrongHandlerException(this, result);


        if (remove.removed() > 0) {
            getConsole().printlnSuc(
                    "Removed " + remove.removed() + " element" + (remove.removed() > 1 ? "s":"") + " from collection"
            );
        }
        getConsole().printlnWarn("No matching elements were found");
    }

}
