package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.results.concrete.GetByResult;
import ru.hanqnero.uni.lab5.commons.util.exceptions.WrongHandlerException;

public class GetByHandler implements ExecutionResultHandler {
    private ConsoleManager console;

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof GetByResult get))
            throw new WrongHandlerException(this, result);

        if (get.getStatus() == ExecutionResult.Status.SUCCESS) {
            console.printlnSuc(get.elementInfo());
        } else {
            console.println("Collection is empty");
        }
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
