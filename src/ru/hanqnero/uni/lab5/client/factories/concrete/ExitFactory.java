package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.AbstractFactory;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.ExitCommand;

public class ExitFactory extends AbstractFactory {
    @Override
    public Command createCommand(String[] tokens) {
        return new ExitCommand();
    }
}
