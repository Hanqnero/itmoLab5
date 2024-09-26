package ru.hanqnero.uni.lab5.client.handlers.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.handlers.AbstractExecutionResultHandler;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.commons.contract.results.concrete.ShowResult;
import ru.hanqnero.uni.lab5.commons.exceptions.WrongHandlerException;

public class ShowResultHandler extends AbstractExecutionResultHandler {

    public ShowResultHandler(ConsoleManager console) {
        super(console);
    }

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
                getConsole().printlnSuc("%d elements in collection:\n"
                        .formatted(show.elementStrings().size()) + messageBody);
            }
            case WARNING -> getConsole().printlnWarn("Collection is empty.");
        }
    }
}
