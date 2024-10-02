package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

import java.time.LocalDateTime;

public record InfoResult(
        Status status,
        long size,
        LocalDateTime creationDate
) implements ExecutionResult {
    @Override
    public Status getStatus() {
        return status;
    }
}
