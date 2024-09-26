package ru.hanqnero.uni.lab5.client.handlers.concrete;

import org.jetbrains.annotations.NotNull;
import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.util.CommandInfo;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.HelpResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongHandlerException;

public class HelpResultHandler extends AbstractExecutionResultHandler {

    public HelpResultHandler(@NotNull ConsoleManager console) {
        super(console);
    }

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof HelpResult helpResult)) {
            throw new WrongHandlerException(this, result);
        }
        switch (helpResult.getStatus()) {
            case ERROR:
                getConsole().printlnErr("Could not get command list from server");
                break;
            case SUCCESS:
                getConsole().printlnSuc(createHelpMessage(helpResult.getServerCommands()));
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
}
