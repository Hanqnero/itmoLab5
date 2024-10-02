package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

public record SaveResult(
        Status status,
        Long size,
        Long savedSize) implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status;
    }
}
