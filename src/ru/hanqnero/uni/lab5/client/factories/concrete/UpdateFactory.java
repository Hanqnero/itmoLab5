package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.collection.Coordinates;
import ru.hanqnero.uni.lab5.collection.MusicGenre;
import ru.hanqnero.uni.lab5.collection.Studio;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.AddCommand;
import ru.hanqnero.uni.lab5.contract.commands.concrete.Update;
import ru.hanqnero.uni.lab5.util.MusicBandSubTypeScanner;
import ru.hanqnero.uni.lab5.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

import java.time.ZonedDateTime;

public class UpdateFactory implements CommandFactory {
    private final MusicBandSubTypeScanner scanner = new MusicBandSubTypeScanner();

    @Override
    public void setConsole(ConsoleManager console) {
        scanner.setConsole(console);
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

        return new Update(id, builder);
    }
}
