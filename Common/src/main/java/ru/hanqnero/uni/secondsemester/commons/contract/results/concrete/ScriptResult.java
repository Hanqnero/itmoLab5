package ru.hanqnero.uni.secondsemester.commons.contract.results.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

import java.io.File;

public record ScriptResult(
        Status status,
        File file,
        boolean isScriptEnd
)
        implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status;
    }
}
