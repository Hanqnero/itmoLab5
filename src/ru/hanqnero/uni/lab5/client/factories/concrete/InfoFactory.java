package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.commands.concrete.InfoCommand;

public class InfoFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) {
        return new InfoCommand();
    }
}
