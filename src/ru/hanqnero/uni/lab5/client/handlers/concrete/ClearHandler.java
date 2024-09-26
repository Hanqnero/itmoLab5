package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.results.concrete.ClearResult;
import ru.hanqnero.uni.lab5.commons.util.exceptions.WrongHandlerException;

public class ClearHandler implements ExecutionResultHandler {
    private ConsoleManager console;

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof ClearResult clear))
            throw new WrongHandlerException(this, result);

        console.printlnSuc("Cleared " + clear.deleted() +  " elements from collection");
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
