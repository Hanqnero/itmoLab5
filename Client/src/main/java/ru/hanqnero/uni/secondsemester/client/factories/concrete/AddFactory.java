package ru.hanqnero.uni.secondsemester.client.factories.concrete;

import ru.hanqnero.uni.secondsemester.client.ConsoleManager;
import ru.hanqnero.uni.secondsemester.client.MusicBandSubTypeScanner;
import ru.hanqnero.uni.secondsemester.client.factories.AbstractFactory;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.commands.concrete.AddCommand;
import ru.hanqnero.uni.secondsemester.client.exceptions.CommandCreationError;
import ru.hanqnero.uni.secondsemester.client.exceptions.SubtypeScanError;

public class AddFactory extends AbstractFactory {
    private final MusicBandSubTypeScanner scanner;

    public AddFactory(ConsoleManager console) {
        this.scanner = new MusicBandSubTypeScanner(console);
    }

    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError {

        AddCommand.Variant variant = AddCommand.Variant.NORMAL;
        if (tokens.length == 2) {
            switch (tokens[1].toLowerCase()) {
                case "--min" -> variant = AddCommand.Variant.MIN;
                case "--max" -> variant = AddCommand.Variant.MAX;
                default -> throw new CommandCreationError();
            }
        } else if (tokens.length != 1) throw new CommandCreationError();

        var builder = scanner.scanBandBuilder();

        return new AddCommand(
                variant,
                builder
        );
    }
}
