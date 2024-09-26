package ru.hanqnero.uni.lab5.commons.results.concrete;

import ru.hanqnero.uni.lab5.commons.CommandInfo;
import ru.hanqnero.uni.lab5.commons.results.ExecutionResult;

import java.time.LocalDateTime;

public class InfoResult implements ExecutionResult {
    private final Status status;
    private final long size;
    private final LocalDateTime creationDate;
    public InfoResult(Status status, long size, LocalDateTime creationDate) {
        this.status = status;
        this.size = size;
        this.creationDate = creationDate;
    }
    @Override
    public Status getStatus() {
        return status;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public String getCommandName() {
        return CommandInfo.INFO.getName();
    }
}
