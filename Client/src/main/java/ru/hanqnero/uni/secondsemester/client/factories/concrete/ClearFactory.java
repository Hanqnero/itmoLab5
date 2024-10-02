package ru.hanqnero.uni.secondsemester.client.factories.concrete;

import ru.hanqnero.uni.secondsemester.client.factories.AbstractFactory;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.ClearCommand;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;
import ru.hanqnero.uni.secondsemester.client.exceptions.SubtypeScanError;

public class ClearFactory extends AbstractFactory {
    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError {
        return new ClearCommand();
    }
}
