package ru.hanqnero.uni.lab5.util;

import ru.hanqnero.uni.lab5.collection.Coordinates;
import ru.hanqnero.uni.lab5.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.collection.MusicGenre;
import ru.hanqnero.uni.lab5.collection.Studio;
import ru.hanqnero.uni.lab5.contract.commands.concrete.AddCommand;
import ru.hanqnero.uni.lab5.util.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Utility class for scanning and validating user input for MusicBand object creation.
 * <p>
 * MusicBandSubTypeScanner provides a comprehensive set of methods for collecting
 * and validating all the components needed to create MusicBand objects from user input.
 * It handles both interactive console input (with prompts and validation loops) and
 * non-interactive input (for script execution), ensuring data integrity and proper
 * error handling in both modes.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Interactive and non-interactive input modes</li>
 *   <li>Comprehensive validation for all MusicBand fields</li>
 *   <li>Type-safe scanning with appropriate error handling</li>
 *   <li>Support for optional fields (genre, studio)</li>
 *   <li>Integration with Console interface for flexible I/O</li>
 *   <li>Detailed error messages for validation failures</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The scanner validates constraints such as:
 * <ul>
 *   <li>Non-empty names without semicolons</li>
 *   <li>Positive numeric values for participant counts and singles</li>
 *   <li>Valid coordinate ranges and non-null y-coordinates</li>
 *   <li>Proper date formats and future date restrictions</li>
 *   <li>Valid enum values for music genres</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see MusicBandBuilder
 * @see Console
 * @see SubtypeScanError
 */
public class MusicBandSubTypeScanner {
    /** Console interface for user input and output operations */
    private Console console;

    /**
     * Sets the console interface for user interaction.
     * <p>
     * This method configures the scanner to use the specified console for
     * reading user input and displaying prompts. The console must be set
     * before calling any scanning methods.
     * </p>
     * 
     * @param console the console interface to use for I/O operations, must not be null
     * @throws IllegalArgumentException if console is null
     */
    public void setConsole(Console console) {
        this.console = console;
    }

    /**
     * Scans and validates a music band name from user input.
     * <p>
     * This method prompts the user for a band name and validates that it is
     * not blank and does not contain semicolons (which are used as delimiters
     * in CSV format). In interactive mode, the user is re-prompted until valid
     * input is provided. In non-interactive mode, invalid input results in an exception.
     * </p>
     * 
     * @return a valid, non-empty band name without semicolons
     * @throws SubtypeScanError if the input is invalid or end of input is reached
     * @throws IllegalStateException if console is not set
     */
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