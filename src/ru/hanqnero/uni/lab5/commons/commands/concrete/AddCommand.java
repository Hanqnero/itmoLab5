package ru.hanqnero.uni.lab5.commons.commands.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.commons.CommandInfo;
import ru.hanqnero.uni.lab5.commons.commands.Command;

public record AddCommand(
        Variant variant,
        MusicBandBuilder builder
) implements Command {
    @Override
    public String getName() {
        return CommandInfo.ADD.getName();
    }

    public enum Variant {
        MIN,
        MAX,
        NORMAL
    }
}
