package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.commons.util.CommandInfo;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

public record RemoveGreaterCommand(MusicBandBuilder builder) implements Command {
    @Override
    public String getName() {
        return CommandInfo.REMOVE_GREATER.getName();
    }
}
