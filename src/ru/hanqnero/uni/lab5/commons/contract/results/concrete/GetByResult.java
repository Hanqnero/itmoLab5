package ru.hanqnero.uni.lab5.commons.contract.results.concrete;

import ru.hanqnero.uni.lab5.commons.util.CommandInfo;
import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;

public record GetByResult(
        Status status,
        String elementInfo
) implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getCommandName() {
        return CommandInfo.GET_BY.getName();
    }
}
