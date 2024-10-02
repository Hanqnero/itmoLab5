package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

public record GetByResult(
        Status status,
        String elementInfo
) implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status;
    }
}
