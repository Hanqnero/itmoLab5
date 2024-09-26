package ru.hanqnero.uni.lab5.commons.results.concrete;

import ru.hanqnero.uni.lab5.commons.CommandInfo;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;

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

    @Override
    public String getCommandName() {
        return CommandInfo.SCRIPT.getName();
    }
}
