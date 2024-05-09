package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.InfoCommand;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

public class InfoFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError {
        return new InfoCommand();
    }
}
