package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.ShowCommand;

public class ShowFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) {
        return new ShowCommand();
    }
}
