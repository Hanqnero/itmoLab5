package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.SaveResult;

public class SaveHandler implements ExecutionResultHandler {
    private ConsoleManager console;
    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof SaveResult save)) {
            return;
        }
        switch (save.getStatus()) {
            case SUCCESS -> console.printlnSuc("Successfully saved %d elements out of %d".formatted(save.savedSize(), save.size()));
        }
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
