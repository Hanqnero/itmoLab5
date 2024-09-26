package ru.hanqnero.uni.lab5.commons.contract.results.concrete;

import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;

public record AddResult(
        Status status,
        long newId
) implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status;
    }
}
