package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.ShowCommand;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

public class ShowFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError {
        return new ShowCommand();
    }
}
