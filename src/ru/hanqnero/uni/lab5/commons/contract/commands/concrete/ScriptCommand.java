package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.util.CommandInfo;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

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
