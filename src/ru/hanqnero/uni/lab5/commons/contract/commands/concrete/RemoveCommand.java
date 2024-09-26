package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.util.CommandInfo;

public record RemoveCommand(
    long id
) implements Command {
    @Override
    public String getName() {
        return CommandInfo.REMOVE.getName();
    }
}
