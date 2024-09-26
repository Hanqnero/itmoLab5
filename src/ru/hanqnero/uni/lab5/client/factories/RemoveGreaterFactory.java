package ru.hanqnero.uni.lab5.client.factories;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.commons.commands.Command;
import ru.hanqnero.uni.lab5.commons.commands.concrete.RemoveGreater;
import ru.hanqnero.uni.lab5.commons.util.MusicBandSubTypeScanner;
import ru.hanqnero.uni.lab5.commons.util.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.util.exceptions.SubtypeScanError;

public class RemoveGreaterFactory implements CommandFactory{
    MusicBandSubTypeScanner scanner = new MusicBandSubTypeScanner();

    @Override
    public void setConsole(ConsoleManager consoleManager) {
        scanner.setConsole(consoleManager);
    }

    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError {
        if (tokens.length != 1) throw new CommandCreationError("Wrong argument count");

        var builder = scanner.scanBandBuilder();
        return new RemoveGreater(builder);
    }
}
