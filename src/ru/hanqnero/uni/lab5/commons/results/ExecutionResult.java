package ru.hanqnero.uni.lab5.commons.results;

import java.io.Serializable;

public interface ExecutionResult extends Serializable {
    enum Status {
        SUCCESS, ERROR, WARNING
    }

    Status getStatus();
    String getCommandName();

}
