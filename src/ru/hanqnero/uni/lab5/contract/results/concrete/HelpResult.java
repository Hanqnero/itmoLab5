package ru.hanqnero.uni.lab5.contract.results.concrete;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

public class HelpResult implements ExecutionResult {
    private final CommandInfo[] serverCommands;
    private final Status status;
    public HelpResult(CommandInfo[] serverCommands) {
       this(serverCommands, Status.SUCCESS);
    }
    public HelpResult(CommandInfo[] serverCommands, Status status) {
        this.serverCommands = serverCommands;
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public CommandInfo[] getServerCommands() {
        return serverCommands;
    }

    @Override
    public String getCommandName() {
        return CommandInfo.HELP.getName();
    }
}
