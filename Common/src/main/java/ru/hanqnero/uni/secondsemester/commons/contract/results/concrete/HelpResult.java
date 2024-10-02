package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

public record HelpResult(
        Status status
) implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status;
    }
}
