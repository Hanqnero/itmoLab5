package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

public record AddCommand(
        Variant variant,
        MusicBandBuilder builder
) implements Command {

    public enum Variant {
        MIN,
        MAX,
        NORMAL
    }
}
