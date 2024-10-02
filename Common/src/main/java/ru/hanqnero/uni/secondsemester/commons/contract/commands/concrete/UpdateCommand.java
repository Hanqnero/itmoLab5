package ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete;

import ru.hanqnero.uni.secondsemester.commons.collection.MusicBandBuilder;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;

public record UpdateCommand(
       long id,
       MusicBandBuilder builder
) implements Command {
}
