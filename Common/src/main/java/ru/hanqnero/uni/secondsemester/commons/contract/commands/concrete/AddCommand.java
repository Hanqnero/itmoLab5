package ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete;

import ru.hanqnero.uni.secondsemester.commons.collection.MusicBandBuilder;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;

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
