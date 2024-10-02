package ru.hanqnero.uni.secondsemester.client.handlers.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.exceptions.WrongHandlerException;
import ru.hanqnero.uni.secondsemester.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.ClearResult;

public class ClearResultHandler extends AbstractExecutionResultHandler {
    public ClearResultHandler(ConsoleManager console) {
        super(console);
    }
    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof ClearResult clear))
            throw new WrongHandlerException(this, result);

        getConsole().printlnSuc("Cleared " + clear.deleted() +  " elements from collection");
    }
}
