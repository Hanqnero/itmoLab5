package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ClientApplication;
import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;

public class ExitResultHandler implements ExecutionResultHandler {
    private ClientApplication client;

    @Override
    public void handleResult(ExecutionResult result) {
        client.stop();
    }

    @Override
    public void setConsole(ConsoleManager console) {
    }

    public void setClient(ClientApplication client) {
        this.client = client;
    }
}
