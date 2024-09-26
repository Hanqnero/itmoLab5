package ru.hanqnero.uni.lab5.server.executors.concrete;

import ru.hanqnero.uni.lab5.commons.CommandInfo;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.commands.concrete.HelpCommand;
import ru.hanqnero.uni.lab5.commons.results.concrete.HelpResult;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.ServerApplication;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.commons.util.exceptions.WrongExecutorForCommandException;

import static ru.hanqnero.uni.lab5.commons.results.ExecutionResult.Status.ERROR;

public class HelpExecutor implements CommandExecutor {

    private ServerApplication server;
    @Override
    public void setCollection(CollectionManager collection) {
        // Do nothing, command does not need collection
    }

    public void setServer(ServerApplication server) {
        this.server = server;
    }

    @Override
    public HelpResult execute(Command command) {
        if (!(command instanceof HelpCommand)) {
            throw new WrongExecutorForCommandException(command, this);
        }
        if (server == null) {
            return new HelpResult(new CommandInfo[0], ERROR);
        }
        return new HelpResult(server.getAvailableCommandInfo());
    }
}
