package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.results.concrete.UpdateResult;
import ru.hanqnero.uni.lab5.commons.util.exceptions.WrongHandlerException;

public class UpdateHandler implements ExecutionResultHandler {
    private ConsoleManager console;

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof UpdateResult update))
            throw new WrongHandlerException(this, result);

        switch (update.getStatus()) {
            case SUCCESS -> console.printlnSuc("Successfully updated element with id " + update.id());
            case ERROR -> console.printlnErr("Could not update element with id " + update.id());
            case WARNING -> console.printlnWarn("No element with id " + update.id() + " in collection");
        }
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
