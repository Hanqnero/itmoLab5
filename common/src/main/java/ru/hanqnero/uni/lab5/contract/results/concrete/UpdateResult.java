package ru.hanqnero.uni.lab5.contract.results.concrete;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

public record UpdateResult(
        Status status,
        long id
) implements ExecutionResult {
    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getCommandName() {
        return CommandInfo.UPDATE.getName();
    }
}
