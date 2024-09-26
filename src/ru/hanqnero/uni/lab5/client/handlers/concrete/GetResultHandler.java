package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.GetByResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongHandlerException;

public class GetResultHandler extends AbstractExecutionResultHandler {
    public GetResultHandler(ConsoleManager console) {
        super(console);
    }

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof GetByResult get))
            throw new WrongHandlerException(this, result);

        if (get.getStatus() == ExecutionResult.Status.SUCCESS) {
            getConsole().printlnSuc(get.elementInfo());
        } else {
            getConsole().println("Collection is empty");
        }
    }
}
