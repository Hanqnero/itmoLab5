package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

public record RemoveCommand(
    long id
) implements Command {
}
