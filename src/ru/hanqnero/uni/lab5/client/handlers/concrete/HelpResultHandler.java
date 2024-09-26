package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.util.CommandInfo;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.HelpResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongHandlerException;

public class HelpResultHandler implements ExecutionResultHandler {
    private ConsoleManager console;

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof HelpResult helpResult)) {
            throw new WrongHandlerException(this, result);
        }
        if (console == null) {
            System.err.println("Console is null.");
            return;
        }
        switch (helpResult.getStatus()) {
            case ERROR:
                console.printlnErr("Could not get command list from server");
                break;
            case SUCCESS:
                console.printlnSuc(createHelpMessage(helpResult.getServerCommands()));
                break;
            default:
                assert false;
        }
    }

    private String createHelpMessage(CommandInfo[] commands) {
        StringBuilder result = new StringBuilder("List of commands:\n");
        for (CommandInfo command : commands) {
            result.append(command.getName()).append(" ").append(command.getDescription()).append("\n");
        }
        return result.toString();
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
