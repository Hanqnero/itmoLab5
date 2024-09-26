package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.results.concrete.RemoveGreaterResult;
import ru.hanqnero.uni.lab5.commons.util.exceptions.WrongHandlerException;

public class RemoveGreaterHandler implements ExecutionResultHandler {
    private ConsoleManager console;
    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof RemoveGreaterResult remove))
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
