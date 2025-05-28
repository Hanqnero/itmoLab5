package ru.hanqnero.uni.lab5.util.exceptions;

import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

public class WrongHandlerException extends RuntimeException {
    public WrongHandlerException(Object handler, ExecutionResult result) {
        super("Can not handle result of command `%s` with handler `%s`".formatted(
                result.getCommandName(), handler.getClass()
        ));
    }
}
