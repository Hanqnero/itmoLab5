package ru.hanqnero.uni.secondsemester.client.handlers.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.InfoResult;
import ru.hanqnero.uni.secondsemester.client.exceptions.WrongHandlerException;

import java.util.Objects;

public class InfoResultHandler extends AbstractExecutionResultHandler {

    public InfoResultHandler(ConsoleManager console) {
        super(console);
    }

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof InfoResult info)) {
            throw new WrongHandlerException(this, result);
        }
        if (Objects.requireNonNull(info.getStatus()) == ExecutionResult.Status.SUCCESS) {
            getConsole().printlnSuc("""
                    Information about collection:
                    Creation date: %s
                    Size: %d elements
                    """.formatted(info.creationDate(), info.size()));
        } else {
            assert false : "NOT REACHABLE";
        }
    }
}
