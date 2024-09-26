package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.SaveResult;

import java.util.Objects;

public class SaveResultHandler extends AbstractExecutionResultHandler {
    public SaveResultHandler(ConsoleManager console) {
        super(console);
    }

    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof SaveResult save)) {
            return;
        }
        if (Objects.requireNonNull(save.getStatus()) == ExecutionResult.Status.SUCCESS) {
            getConsole().printlnSuc("Successfully saved %d elements out of %d".formatted(save.savedSize(), save.size()));
        }
    }
}
