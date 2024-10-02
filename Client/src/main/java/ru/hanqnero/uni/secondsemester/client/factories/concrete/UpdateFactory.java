package ru.hanqnero.uni.secondsemester.client.factories.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.MusicBandSubTypeScanner;
import ru.hanqnero.uni.secondsemester.client.factories.AbstractFactory;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.UpdateCommand;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;
import ru.hanqnero.uni.secondsemester.client.exceptions.SubtypeScanError;

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
