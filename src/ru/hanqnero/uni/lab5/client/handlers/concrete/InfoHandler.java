package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.results.concrete.InfoResult;
import ru.hanqnero.uni.lab5.commons.util.exceptions.WrongHandlerException;

import java.util.Objects;

public class InfoHandler implements ExecutionResultHandler {
    private ConsoleManager console;
    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof InfoResult info)) {
            throw new WrongHandlerException(this, result);
        }
        if (Objects.requireNonNull(info.getStatus()) == ExecutionResult.Status.SUCCESS) {
            console.printlnSuc("""
                    Information about collection:
                    Creation date: %s
                    Size: %d elements
                    """.formatted(info.getCreationDate(), info.getSize()));
        } else {
            assert false : "NOT REACHABLE";
        }
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
