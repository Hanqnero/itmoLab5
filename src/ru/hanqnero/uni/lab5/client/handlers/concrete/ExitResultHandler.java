package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ClientApplication;
import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;

public class ExitResultHandler extends AbstractExecutionResultHandler {
    private final ClientApplication client;

    public ExitResultHandler(ClientApplication client) {
        this.client = client;
    }

    @Override
    public void handleResult(ExecutionResult result) {
        client.stop();
    }
}
