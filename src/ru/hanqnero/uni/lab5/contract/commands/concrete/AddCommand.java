package ru.hanqnero.uni.lab5.contract.commands.concrete;

import ru.hanqnero.uni.lab5.collection.Coordinates;
import ru.hanqnero.uni.lab5.collection.MusicGenre;
import ru.hanqnero.uni.lab5.collection.Studio;
import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;

import java.time.ZonedDateTime;

public record AddCommand(
        String name,
        Coordinates coordinates,
        Long participants,
        int singles,
        ZonedDateTime estDate,
        Studio studio,
        MusicGenre genre
) implements Command {
    @Override
    public String getName() {
        return CommandInfo.ADD.getName();
    }
}
