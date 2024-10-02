package ru.hanqnero.uni.secondsemester.server.executors;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

public interface CommandExecutor {
    ExecutionResult execute(Command command);
}
