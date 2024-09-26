package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.ClearResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongHandlerException;

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
