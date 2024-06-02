package ru.hanqnero.uni.lab5.contract.commands.concrete;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.CommandInfo;

public record RemoveId(
    long id
) implements Command {
    @Override
    public String getName() {
        return CommandInfo.REMOVE.getName();
    }
}
