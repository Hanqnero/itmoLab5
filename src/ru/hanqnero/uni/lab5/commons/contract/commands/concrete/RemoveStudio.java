package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.collection.Studio;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

public record RemoveStudio(
        Studio studio
) implements Command {
}
