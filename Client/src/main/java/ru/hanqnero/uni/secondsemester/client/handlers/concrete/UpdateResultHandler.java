package ru.hanqnero.uni.secondsemester.client.handlers.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.exceptions.WrongHandlerException;
import ru.hanqnero.uni.secondsemester.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.UpdateResult;

public class UpdateResultHandler extends AbstractExecutionResultHandler {
    public UpdateResultHandler(ConsoleManager console) {
        super(console);
    }

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof UpdateResult update))
            throw new WrongHandlerException(this, result);

        switch (update.getStatus()) {
            case SUCCESS -> getConsole().printlnSuc("Successfully updated element with id " + update.id());
            case ERROR -> getConsole().printlnErr("Could not update element with id " + update.id());
            case WARNING -> getConsole().printlnWarn("No element with id " + update.id() + " in collection");
        }
    }

}
