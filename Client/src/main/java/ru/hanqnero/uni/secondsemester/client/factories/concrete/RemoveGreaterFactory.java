package ru.hanqnero.uni.secondsemester.client.factories.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.MusicBandSubTypeScanner;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;
import ru.hanqnero.uni.secondsemester.client.exceptions.SubtypeScanError;
import ru.hanqnero.uni.secondsemester.client.factories.AbstractFactory;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.RemoveGreaterCommand;

public class RemoveGreaterFactory extends AbstractFactory {
    private final MusicBandSubTypeScanner scanner;

    public RemoveGreaterFactory(ConsoleManager console) {
        scanner = new MusicBandSubTypeScanner(console);
    }

    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError {
        if (tokens.length != 1) throw new CommandCreationError("Wrong argument count");

        var builder = scanner.scanBandBuilder();
        return new RemoveGreaterCommand(builder);
    }
}
