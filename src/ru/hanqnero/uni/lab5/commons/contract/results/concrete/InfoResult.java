package ru.hanqnero.uni.lab5.commons.contract.results.concrete;

import ru.hanqnero.uni.lab5.commons.contract.results.ExecutionResult;

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
