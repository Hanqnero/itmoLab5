package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.RemoveResult;
import ru.hanqnero.uni.lab5.util.exceptions.WrongHandlerException;

public class RemoveHandler implements ExecutionResultHandler {
    private ConsoleManager console;
    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof RemoveResult remove))
            throw new WrongHandlerException(this, result);


        if (remove.removed() > 0) {
            console.printlnSuc(
                    "Removed " + remove.removed() + " element" + (remove.removed() > 1 ? "s":"") + " from collection"
            );
        }
        console.printlnWarn("No matching elements were found");
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
