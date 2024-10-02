package ru.hanqnero.uni.secondsemester.server.exceptions;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.server.executors.CommandExecutor;


public class WrongExecutorForCommandException extends RuntimeException{
    public WrongExecutorForCommandException(Command command, CommandExecutor executor) {
        super("Tried to execute command of type `%s` with executor `%s` which is unsupported"
                .formatted(command.getClass(), executor.getClass()));
    }
}
