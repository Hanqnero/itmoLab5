package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.InfoCommand;

public class InfoFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) {
        return new InfoCommand();
    }
}
