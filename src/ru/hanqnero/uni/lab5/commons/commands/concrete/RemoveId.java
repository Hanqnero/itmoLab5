package ru.hanqnero.uni.lab5.commons.commands.concrete;

import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.CommandInfo;

public record RemoveId(
    long id
) implements Command {
    @Override
    public String getName() {
        return CommandInfo.REMOVE.getName();
    }
}
