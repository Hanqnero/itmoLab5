package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.contract.results.concrete.ShowResult;
import ru.hanqnero.uni.lab5.util.exceptions.WrongHandlerException;

public class ShowHandler implements ExecutionResultHandler {
    private ConsoleManager console;
    @Override
    public void handleResult(ExecutionResult result) {
        if (!(result instanceof ShowResult show)) {
            throw new WrongHandlerException(this, result);
        }

        switch (show.getStatus()) {
            case SUCCESS -> {
                String messageBody = show.elementStrings().stream().reduce("",
                        (s1, s2) -> s1.concat(s2).concat("\n")
                );
                console.printlnSuc("%d elements in collection:\n"
                        .formatted(show.elementStrings().size()) + messageBody);
            }
            case WARNING -> console.printlnWarn("Collection is empty.");
        }
    }

    @Override
    public void setConsole(ConsoleManager console) {
        this.console = console;
    }
}
