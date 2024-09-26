package ru.hanqnero.uni.lab5.commons.exceptions;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;

public class WrongExecutorForCommandException extends RuntimeException{
    public WrongExecutorForCommandException(Command command, CommandExecutor executor) {
        super("Tried to execute command of type `%s` with executor `%s` which is unsupported"
                .formatted(command.getClass(), executor.getClass()));
    }
}
