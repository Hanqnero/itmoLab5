package ru.hanqnero.uni.lab5.contract.results;

public interface ExecutionResult {
    enum Status {
        SUCCESS, ERROR, WARNING;
    }

    Status getStatus();
    String getCommandName();
}
