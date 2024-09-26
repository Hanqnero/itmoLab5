package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.ClearCommand;
import ru.hanqnero.uni.lab5.commons.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.exceptions.SubtypeScanError;

public class ClearFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError {
        return new ClearCommand();
    }
}
