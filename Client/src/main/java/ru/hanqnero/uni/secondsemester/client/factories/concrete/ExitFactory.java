package ru.hanqnero.uni.secondsemester.client.factories.concrete;

import ru.hanqnero.uni.secondsemester.client.factories.AbstractFactory;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.ExitCommand;

public class ExitFactory extends AbstractFactory {
    @Override
    public Command createCommand(String[] tokens) {
        return new ExitCommand();
    }
}
