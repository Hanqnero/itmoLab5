package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.AddResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongHandlerException;

public class AddResultHandler extends AbstractExecutionResultHandler {

    public AddResultHandler(ConsoleManager console) {
        super(console);
    }

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof AddResult add)) {
            throw new WrongHandlerException(this, result);
        }
        switch (add.getStatus()) {
            case SUCCESS -> getConsole().printlnSuc("Successfully added element to collection with id: %d".formatted(add.newId()));
            case ERROR -> getConsole().printlnErr("Could not add element to collection");
        }
    }
}
