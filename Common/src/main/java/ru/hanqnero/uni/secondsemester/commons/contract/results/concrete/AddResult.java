package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

public record AddResult(
        Status status,
        long newId
) implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status;
    }
}
