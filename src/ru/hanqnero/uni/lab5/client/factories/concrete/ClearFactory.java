package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.commands.concrete.Clear;
import ru.hanqnero.uni.lab5.commons.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.util.exceptions.SubtypeScanError;

public class ClearFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError {
        return new Clear();
    }
}
