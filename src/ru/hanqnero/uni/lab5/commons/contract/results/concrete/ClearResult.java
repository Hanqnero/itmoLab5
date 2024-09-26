package ru.hanqnero.uni.lab5.commons.contract.results.concrete;

import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;

public record ClearResult(
        Status status,
        long deleted
) implements ExecutionResult {
    @Override
    public Status getStatus() {
        return status;
    }
}
