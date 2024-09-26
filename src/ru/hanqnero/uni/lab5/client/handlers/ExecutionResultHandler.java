package ru.hanqnero.uni.lab5.client.handlers;

import ru.hanqnero.uni.lab5.client.ClientApplication;
import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;

public interface ExecutionResultHandler {
    void handleResult(ExecutionResult result);
    void setConsole(ConsoleManager console);
    default void setClient(ClientApplication client) {}
}
