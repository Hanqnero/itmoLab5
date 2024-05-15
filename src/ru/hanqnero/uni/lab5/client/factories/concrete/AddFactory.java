package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.collection.Coordinates;
import ru.hanqnero.uni.lab5.collection.MusicGenre;
import ru.hanqnero.uni.lab5.collection.Studio;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.commands.concrete.AddCommand;
import ru.hanqnero.uni.lab5.util.MusicBandSubTypeScanner;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

import java.time.ZonedDateTime;

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
    public Command createCommand(String[] tokens) throws SubtypeScanError {
            String name = scanner.scanName();
            Coordinates coordinates = scanner.scanCoordinates();
            Long numberOfParticipants = scanner.scanNumberOfParticipants();
            int singlesCount = scanner.scanSinglesCount();
            ZonedDateTime establishmentDate = scanner.scanEstDate();

            Studio studio = scanner.scanStudio();
            MusicGenre genre = scanner.scanMusicGenre();

            return new AddCommand(
                    name,
                    coordinates,
                    numberOfParticipants,
                    singlesCount,
                    establishmentDate,
                    studio,
                    genre
            );
    }
}
