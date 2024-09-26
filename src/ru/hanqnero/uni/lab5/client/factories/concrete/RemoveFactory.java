package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.commons.collection.Studio;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.commands.concrete.RemoveId;
import ru.hanqnero.uni.lab5.commons.commands.concrete.RemoveStudio;
import ru.hanqnero.uni.lab5.commons.util.MusicBandSubTypeScanner;
import ru.hanqnero.uni.lab5.commons.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.util.exceptions.SubtypeScanError;

public class RemoveFactory implements CommandFactory {
    private final MusicBandSubTypeScanner scanner = new MusicBandSubTypeScanner();

    @Override
    public void setConsole(ConsoleManager console) {
        scanner.setConsole(console);
    }

    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError {
        if (tokens.length == 1)
            throw new CommandCreationError("Not enough arguments");

        if (tokens[1].equalsIgnoreCase("--id")) {
            long id;
            try {
                id = Long.parseLong(tokens[2]);
            } catch (NumberFormatException e) {
                throw new CommandCreationError("Could not parse id long");
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new CommandCreationError("Not enough arguments");
            }

            return new RemoveId(id);
        }

        if (tokens[1].equalsIgnoreCase("--studio")) {
            Studio s = scanner.scanStudio();
            return new RemoveStudio(s);
        }

        throw new CommandCreationError("Wrong command format");
    }
}
