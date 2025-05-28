package ru.hanqnero.uni.lab5.contract.results.concrete;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

public record ClearResult(
        Status status,
        long deleted
) implements ExecutionResult {
    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getCommandName() {
        return CommandInfo.CLEAR.getName();
    }
}
