package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

public record RemoveResult(
        Status status,
        long removed
) implements ExecutionResult {
    @Override
    public Status getStatus() {
        return status;
    }
}
