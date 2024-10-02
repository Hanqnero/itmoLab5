package ru.hanqnero.uni.secondsemester.commons.contract.results;

import java.io.Serializable;

public interface ExecutionResult extends Serializable {
    enum Status {
        SUCCESS, ERROR, WARNING
    }

    Status getStatus();
}
