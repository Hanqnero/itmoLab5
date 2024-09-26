package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.util.CommandInfo;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

public class ShowCommand implements Command {
    @Override
    public String getName() {
        return CommandInfo.SHOW.getName();
    }
}
