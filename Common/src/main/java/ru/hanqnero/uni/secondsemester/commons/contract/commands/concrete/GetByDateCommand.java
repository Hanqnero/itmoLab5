package ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;

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
