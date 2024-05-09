package ru.hanqnero.uni.lab5.contract.commands.client;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;

public class Exit implements Command {
    @Override
    public String getName() {
        return CommandInfo.EXIT.getName();
    }
}
