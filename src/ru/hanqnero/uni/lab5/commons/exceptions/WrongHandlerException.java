package ru.hanqnero.uni.lab5.commons.exceptions;

import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;

public class WrongHandlerException extends RuntimeException {
    public WrongHandlerException(ExecutionResultHandler handler, ExecutionResult result) {
        super("Can not handle result of command `%s` with handler `%s`".formatted(
                result.getCommandName(), handler.getClass()
        ));
    }
}
