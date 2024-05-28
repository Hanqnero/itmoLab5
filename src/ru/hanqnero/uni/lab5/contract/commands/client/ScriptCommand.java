package ru.hanqnero.uni.lab5.contract.commands.client;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;

import java.io.File;

public record ScriptCommand(
        File filename,
        boolean isScriptEnd
)
        implements Command {
    @Override
    public String getName() {
        return CommandInfo.SCRIPT.getName();
    }
}
