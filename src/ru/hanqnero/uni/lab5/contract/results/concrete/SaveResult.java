package ru.hanqnero.uni.lab5.contract.results.concrete;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

public record SaveResult(Status status, Long size, Long savedSize) implements ExecutionResult {
    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getCommandName() {
        return CommandInfo.SAVE.getName();
    }
}
