package ru.hanqnero.uni.lab5.contract.commands.concrete;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;

public class HelpCommand implements Command {
    @Override
    public String getName() {
        return CommandInfo.HELP.getName();
    }
}
