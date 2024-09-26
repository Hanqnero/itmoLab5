package ru.hanqnero.uni.lab5.client.handlers.concrete;

import org.jetbrains.annotations.NotNull;
import ru.hanqnero.uni.lab5.client.ClientApplication;
import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.HelpResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongHandlerException;

public class HelpResultHandler extends AbstractExecutionResultHandler {
    private final ClientApplication client;

    public HelpResultHandler(@NotNull ConsoleManager console, @NotNull ClientApplication client) {
        super(console);
        this.client = client;
    }

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof HelpResult help)) {
            throw new WrongHandlerException(this, result);
        }

        getConsole().println(createHelpMessage());

    }

    private String createHelpMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available commands:\n");
        client.getCommandRegistryEntries().forEach(entry -> sb.append(entry.createHelpString()).append("\n"));
        return sb.toString();
    }
}
