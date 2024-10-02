package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

import java.util.List;

public record ShowResult(
        Status status,
        List<String> elementStrings
) implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status();
    }
}
