package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.AddCommand;
import ru.hanqnero.uni.lab5.commons.util.MusicBandSubTypeScanner;
import ru.hanqnero.uni.lab5.commons.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.exceptions.SubtypeScanError;

public class AddFactory implements CommandFactory {
    private final MusicBandSubTypeScanner scanner;

    public AddFactory() {
        this.scanner = new MusicBandSubTypeScanner();
    }

    @Override
    public void setConsole(ConsoleManager consoleManager) {
        scanner.setConsole(consoleManager);
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
