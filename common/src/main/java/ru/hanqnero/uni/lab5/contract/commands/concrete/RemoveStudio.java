package ru.hanqnero.uni.lab5.contract.commands.concrete;

import ru.hanqnero.uni.lab5.collection.Studio;
import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;

public record RemoveStudio(
        Studio studio
) implements Command {
    @Override
    public String getName() {
        return CommandInfo.REMOVE.getName();
    }
}
