package ru.hanqnero.uni.lab5.contract.commands.concrete;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;

public record GetByDate(
        Variant var,
        DateType type
) implements Command {
    public enum Variant {
        MIN,MAX
    }
    public enum DateType {
        CREATION, ESTABLISHMENT
    }

    @Override
    public String getName() {
        return CommandInfo.GET_BY.getName();
    }
}
