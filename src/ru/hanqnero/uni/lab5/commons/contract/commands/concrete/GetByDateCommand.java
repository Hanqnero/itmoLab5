package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

public record GetByDateCommand(
        Variant var,
        DateType type
) implements Command {
    public enum Variant {
        MIN,MAX
    }
    public enum DateType {
        CREATION, ESTABLISHMENT
    }
}
