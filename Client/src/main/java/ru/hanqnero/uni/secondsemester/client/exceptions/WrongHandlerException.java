package ru.hanqnero.uni.secondsemester.client.exceptions;

import ru.hanqnero.uni.secondsemester.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

public class WrongHandlerException extends RuntimeException {
    public WrongHandlerException(ExecutionResultHandler handler, ExecutionResult result) {
        super("Can not handle result of type `%s` with handler `%s`".formatted(
                result.getClass(), handler.getClass()
        ));
    }
}
