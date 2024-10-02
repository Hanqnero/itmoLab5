package ru.hanqnero.uni.secondsemester.client.handlers.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.client.exceptions.WrongHandlerException;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.GetByResult;

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
