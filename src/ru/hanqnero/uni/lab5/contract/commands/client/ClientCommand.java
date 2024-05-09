package ru.hanqnero.uni.lab5.contract.commands.client;

import ru.hanqnero.uni.lab5.client.ClientApplication;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;

public interface ClientCommand extends Command {
    ExecutionResult execute(ClientApplication client);
}
