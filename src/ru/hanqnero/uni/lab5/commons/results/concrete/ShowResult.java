package ru.hanqnero.uni.lab5.commons.results.concrete;

import ru.hanqnero.uni.lab5.commons.CommandInfo;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;

import java.util.List;

public record ShowResult(Status status, List<String> elementStrings) implements ExecutionResult {

    @Override
    public Status getStatus() {
        return status();
    }

    @Override
    public String getCommandName() {
        return CommandInfo.SHOW.getName();
    }
}
