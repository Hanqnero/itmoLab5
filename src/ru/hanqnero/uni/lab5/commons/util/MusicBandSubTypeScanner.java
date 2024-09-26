package ru.hanqnero.uni.lab5.commons.util;

import ru.hanqnero.uni.lab5.client.ConsoleManager;
import ru.hanqnero.uni.lab5.commons.collection.Coordinates;
import ru.hanqnero.uni.lab5.commons.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.commons.collection.MusicGenre;
import ru.hanqnero.uni.lab5.commons.collection.Studio;
import ru.hanqnero.uni.lab5.commons.util.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.lab5.commons.util.exceptions.SubtypeScanError;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Predicate;

public class MusicBandSubTypeScanner {
    private ConsoleManager console;

    public void setConsole(ConsoleManager console) {
        this.console = console;
    }

    public String scanName() throws SubtypeScanError {
        try {
            assert console != null;
            String line = console.nextLine("Name: ");
            if (console.isInteractive()) {
                while (line.isBlank() || line.contains(";")) {
                    line = console.nextLine("""
                            Name cannot contain semicolons `;`
                            :\s""");
                }
            } else {
                console.println(line);
                if (line.isBlank()) {
                    throw new SubtypeScanError(line, "Name");
                }
            }
            return line;
        } catch (ConsoleEmptyException e) {
            throw new SubtypeScanError("<EOF reached>", "Name");
        }
    }

    private Optional<Coordinates> tryParseCoordinates(String string) {
        String[] words = string.split("\\s+");
        try {
            long x = Long.parseLong(words[0]);
            Integer y = Integer.parseInt(words[1]);

            return Optional.of(new Coordinates(x, y));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public Coordinates scanCoordinates() throws SubtypeScanError {
        try {
            String line = console.nextLine("Coordinates: ");
            Optional<Coordinates> coordinates = tryParseCoordinates(line);
            if (console.isInteractive()) {
                while (coordinates.isEmpty()) {
                    line = console.nextLine("Coordinates: ");
                    coordinates = tryParseCoordinates(line);
                }
            } else {
                console.println(line);
                if (coordinates.isEmpty()) {
                    throw new SubtypeScanError(line, "Coordinates");
                }
            }
            return coordinates.get();
        } catch (ConsoleEmptyException e) {
            throw new SubtypeScanError("<EOF reached>", "Name");
        }
    }

    public Studio scanStudio() throws SubtypeScanError {
        String name;
        String address;
        try {

//            String line = console.nextLine("""
//                    Studio. Press enter for NULL Studio
//                    Send any text to start creating Studio
//                    :\s""");
//            if (line.isBlank())
//                return null;
//            else {
                name = console.nextLine("Name, for NULL leave blank: ");
                if (console.isInteractive()) {
                    while (name.contains(";")) {
                        name = console.nextLine("""
                                Stings cannot contain `;'
                                :\s""");
                    }
                } else {
                    console.println(name);
                    if (name.contains(";"))
                        throw new SubtypeScanError(name, "String");
                }
                if (name.isBlank() || name.equals("NULL"))
                    name = null;

                address = console.nextLine("Address, for NULL leave blank: ");

                if (console.isInteractive()) {
                    while (address.contains(";")) {
                        address = console.nextLine("""
                                Stings cannot contain `;'
                                :\s""");
                    }
                } else {
                    console.println(address);
                    if (address.contains(";"))
                        throw new SubtypeScanError(address, "String");
                }
                if (address.isBlank() || address.equals("NULL"))
                    address = null;

                if (name == null && address == null)
                    return null;

                return new Studio().setName(name).setAddress(address);
//            }
        } catch (ConsoleEmptyException e) {
            throw new SubtypeScanError("<EOF reached>", "Studio");
        }
    }

    private Optional<MusicGenre> tryParseMusicGenre(String string) {
        try {
            MusicGenre genre = MusicGenre.valueOf(string);
            return Optional.of(genre);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private Optional<ZonedDateTime> tryParseDateTime(String string) {
        if (string.isBlank() || string.equalsIgnoreCase("now")) return Optional.of(ZonedDateTime.now());
        try {
            // 2011-12-03T10:15:30+01:00[Europe/ Paris]
            ZonedDateTime dateTime = ZonedDateTime.parse(string);
            return Optional.of(dateTime);
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public ZonedDateTime scanEstDate() throws SubtypeScanError {
        try {
            String line = console.nextLine("""
                            Establishment date
                            Format: `2005-02-14T13:37:00+05`
                            blank line or 'now' for current time
                            :\s""");
            Optional<ZonedDateTime> dateTimeOptional = tryParseDateTime(line);
            if (console.isInteractive()) {
                while (dateTimeOptional.isEmpty()) {
                    line = console.nextLine("""
                            Establishment date
                            Format: `2005-02-14T13:37:00+05`
                            :\s""");
                    dateTimeOptional = tryParseDateTime(line);
                }
            } else {
                console.println(line);
                if (dateTimeOptional.isEmpty()) {
                    throw new SubtypeScanError(line, "Date");
                }
            }
            return dateTimeOptional.orElseThrow(RuntimeException::new);
        } catch (ConsoleEmptyException e) {
            throw new SubtypeScanError("<EOF reached>", "Name");
        }
    }

    public MusicGenre scanMusicGenre() throws SubtypeScanError {
        try {
            String line = console.nextLine("""
                            Music Genre, one of: POST_ROCK, PUNK_ROCK, PROGRESSIVE_ROCK, PSYCHEDELIC_ROCK
                            or blank for NULL
                            :\s""");
            if (line.isBlank() || line.equals("NULL")) return null;
            Optional<MusicGenre> genreOptional = tryParseMusicGenre(line);
            if (console.isInteractive()) {
                while (genreOptional.isEmpty()) {
                    line = console.nextLine("""
                            Music Genre, one of: POST_ROCK, PUNK_ROCK, PROGRESSIVE_ROCK, PSYCHEDELIC_ROCK
                            or blank for NULL
                            :\s""");
                    if (line.isBlank()) return null;
                    genreOptional = tryParseMusicGenre(line);
                }
            } else {
                console.println(line);
                if (genreOptional.isEmpty()) {
                    throw new SubtypeScanError(line, "MusicGenre");
                }
            }
            return genreOptional.get();
        } catch (ConsoleEmptyException e) {
            throw new SubtypeScanError("<EOF reached>", "Name");
        }
    }

    private Optional<Integer> tryParseMatchingInteger(String string, Predicate<Integer> predicate) {
        try {
            int x = Integer.parseInt(string);
            if (predicate.test(x)) {
                return Optional.of(x);
            } else
                return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public int scanSinglesCount() throws SubtypeScanError {
        try {
            String line = console.nextLine("Singles count: ");
            Optional<Integer> optional = tryParseMatchingInteger(line, x -> x > 0);
            if (console.isInteractive()) {
                while (optional.isEmpty()) {
                    line = console.nextLine("""
                            Singles count
                            Positive number
                            :\s""");
                    optional = tryParseMatchingInteger(line, x -> x > 0);
                }
            } else {
                console.println(line);
                if (optional.isEmpty()) {
                    throw new SubtypeScanError(line, "Singles Count");
                }
            }
            return optional.get();
        } catch (ConsoleEmptyException e) {
            throw new SubtypeScanError("<EOF reached>", "Name");
        }
    }

    private Optional<Long> tryParseMatchingLong(String string, Predicate<Long> predicate) {
        try {
            Long x = Long.parseLong(string);
            if (predicate.test(x)) {
                return Optional.of(x);
            } else
                return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Long scanNumberOfParticipants() throws SubtypeScanError {
        try {
            String line = console.nextLine("Number of participants: ");
            Optional<Long> optional = tryParseMatchingLong(line, x -> x > 0);
            if (console.isInteractive()) {
                while (optional.isEmpty()) {
                    line = console.nextLine("""
                            Number of participants
                            Positive number
                            :\s""");
                    optional = tryParseMatchingLong(line, x -> x > 0);
                }
            } else {
                console.println(line);
                if (optional.isEmpty()) {
                    throw new SubtypeScanError(line, "Number of participants");
                }
            }
            return optional.get();
        } catch (ConsoleEmptyException e) {
            throw new SubtypeScanError("<EOF reached>", "Name");
        }
    }

    public MusicBandBuilder scanBandBuilder() throws SubtypeScanError {
        var scanner = this;

        if (scanner.console == null)
            throw new SubtypeScanError("Console of band scanner is null");

        String name = scanner.scanName();
        Coordinates coordinates = scanner.scanCoordinates();
        Long numberOfParticipants = scanner.scanNumberOfParticipants();
        int singlesCount = scanner.scanSinglesCount();
        ZonedDateTime establishmentDate = scanner.scanEstDate();

        Studio studio = scanner.scanStudio();
        MusicGenre genre = scanner.scanMusicGenre();

        return new MusicBandBuilder(
                name,
                coordinates,
                numberOfParticipants,
                singlesCount,
                establishmentDate
        )
                .setStudio(studio)
                .setGenre(genre);
    }
}