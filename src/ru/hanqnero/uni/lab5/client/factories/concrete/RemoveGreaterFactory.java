package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.factories.AbstractFactory;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.RemoveGreaterCommand;
import ru.hanqnero.uni.lab5.commons.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.exceptions.SubtypeScanError;
import ru.hanqnero.uni.lab5.commons.util.MusicBandSubTypeScanner;

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
