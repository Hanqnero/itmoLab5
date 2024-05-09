package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.AddResult;
import ru.hanqnero.uni.lab5.util.exceptions.WrongHandlerException;

public class AddResultHandler implements ExecutionResultHandler {
    private ConsoleManager console;
    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof AddResult add)) {
            throw new WrongHandlerException(this, result);
        }
        switch (add.getStatus()) {
            case SUCCESS -> console.printlnSuc("Successfully added element to collection with id: %d".formatted(add.getId()));
            case ERROR -> console.printlnErr("Could not add element to collection");
        }
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
