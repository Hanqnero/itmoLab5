package ru.hanqnero.uni.secondsemester.client.factories.concrete;

import ru.hanqnero.uni.secondsemester.client.factories.AbstractFactory;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.ShowCommand;

public class ShowFactory extends AbstractFactory {
    @Override
    public Command createCommand(String[] tokens) {
        return new ShowCommand();
    }
}
