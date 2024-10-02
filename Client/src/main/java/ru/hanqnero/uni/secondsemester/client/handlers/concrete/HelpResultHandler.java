package ru.hanqnero.uni.secondsemester.client.handlers.concrete;

import ru.hanqnero.uni.secondsemester.client.ClientApplication;
import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.contract.results.concrete.HelpResult;
import ru.hanqnero.uni.secondsemester.client.exceptions.WrongHandlerException;

public class HelpResultHandler extends AbstractExecutionResultHandler {
    private final ClientApplication client;

    public HelpResultHandler(ConsoleManager console, ClientApplication client) {
        super(console);
        this.client = client;
    }

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof HelpResult)) {
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
