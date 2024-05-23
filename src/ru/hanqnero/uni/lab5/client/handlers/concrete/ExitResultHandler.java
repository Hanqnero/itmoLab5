package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ClientApplication;
import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;

public class ExitResultHandler implements ExecutionResultHandler {
    private ConsoleManager console;
    private ClientApplication client;

    @Override
    public void handleResult(ExecutionResult result) {
        client.stop();
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
    public void setClient(ClientApplication client) {
        this.client = client;
    }
}
