package ru.hanqnero.uni.lab5.contract.results.concrete;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

public class AddResult implements ExecutionResult {
    private final Status status;
    private final long newId;
    public AddResult(Status status, Long newId) {
        this.status = status;
        this.newId = newId;
    }
    @Override
    public Status getStatus() {
        return status;
    }

    public long getId() {
        return newId;
    }

    @Override
    public String getCommandName() {
        return CommandInfo.ADD.getName();
    }
}
