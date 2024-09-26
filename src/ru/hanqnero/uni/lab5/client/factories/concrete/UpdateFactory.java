package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.factories.AbstractFactory;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.UpdateCommand;
import ru.hanqnero.uni.lab5.commons.util.MusicBandSubTypeScanner;
import ru.hanqnero.uni.lab5.commons.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.exceptions.SubtypeScanError;

public class UpdateFactory extends AbstractFactory {
    private final MusicBandSubTypeScanner scanner;

    public UpdateFactory(ConsoleManager console) {
        scanner = new MusicBandSubTypeScanner(console);
    }

    @Override
    public Command createCommand(String[] tokens) throws CommandCreationError, SubtypeScanError {
        if (tokens.length != 2) {
            throw new CommandCreationError("Wrong argument count");
        }
        long id;
        try {
            id = Long.parseLong(tokens[1]);
        } catch (NumberFormatException e) {
            throw new CommandCreationError("Wrong id format");
        }
        var builder = scanner.scanBandBuilder();

        return new UpdateCommand(id, builder);
    }
}
