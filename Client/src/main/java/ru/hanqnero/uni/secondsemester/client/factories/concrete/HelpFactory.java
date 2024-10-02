package ru.hanqnero.uni.secondsemester.client.factories.concrete;

import ru.hanqnero.uni.secondsemester.client.factories.AbstractFactory;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.HelpCommand;

public class HelpFactory extends AbstractFactory {

    @Override
    public Command createCommand(String[] tokens) {
        return new HelpCommand();
    }
}
