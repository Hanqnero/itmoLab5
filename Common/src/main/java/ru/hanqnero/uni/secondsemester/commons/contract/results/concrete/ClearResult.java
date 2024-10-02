package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

public record ClearResult(
        Status status,
        long deleted
) implements ExecutionResult {
    @Override
    public Status getStatus() {
        return status;
    }
}
