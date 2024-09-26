package ru.hanqnero.uni.lab5.commons.commands.concrete;

import ru.hanqnero.uni.lab5.commons.CommandInfo;
import ru.hanqnero.uni.lab5.commons.commands.Command;

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
