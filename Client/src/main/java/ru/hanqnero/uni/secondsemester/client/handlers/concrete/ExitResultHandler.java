package ru.hanqnero.uni.secondsemester.client.handlers.concrete;

import ru.hanqnero.uni.secondsemester.client.ClientApplication;
import ru.hanqnero.uni.secondsemester.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

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
