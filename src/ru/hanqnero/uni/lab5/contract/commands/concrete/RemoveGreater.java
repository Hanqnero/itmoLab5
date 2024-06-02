package ru.hanqnero.uni.lab5.contract.commands.concrete;

import ru.hanqnero.uni.lab5.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;

public record RemoveGreater(MusicBandBuilder builder) implements Command {
    @Override
    public String getName() {
        return CommandInfo.REMOVE_GREATER.getName();
    }
}
