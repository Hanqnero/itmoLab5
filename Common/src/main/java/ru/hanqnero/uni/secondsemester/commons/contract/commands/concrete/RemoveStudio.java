package ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete;

import ru.hanqnero.uni.secondsemester.commons.collection.Studio;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;

public record RemoveStudio(
        Studio studio
) implements Command {
}
