package ru.hanqnero.uni.lab5.commons.commands.concrete;

import ru.hanqnero.uni.lab5.commons.CommandInfo;
import ru.hanqnero.uni.lab5.commons.commands.Command;

public class InfoCommand implements Command {
    @Override
    public String getName() {
        return CommandInfo.INFO.getName();
    }
}
