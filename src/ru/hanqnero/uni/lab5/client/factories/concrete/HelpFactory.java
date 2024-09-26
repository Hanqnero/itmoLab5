package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.commands.concrete.HelpCommand;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;

public class HelpFactory implements CommandFactory {

    @Override
    public Command createCommand(String[] tokens) {
        return new HelpCommand();
    }
}
