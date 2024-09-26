package ru.hanqnero.uni.lab5.client.handlers;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;

public abstract class AbstractExecutionResultHandler implements ExecutionResultHandler {
    private final ConsoleManager console;

    public AbstractExecutionResultHandler(ConsoleManager console) {
        this.console = console;
    }

    public AbstractExecutionResultHandler() {
        this.console = null;
    }

    public ConsoleManager getConsole() {
        return console;
    }

    @Override
    public abstract void handleResult(ExecutionResult result);
}
