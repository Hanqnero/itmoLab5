package ru.hanqnero.uni.lab5.collection;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class MusicBandORM {
    // NULLS ARE SAVED AS BLANK
    public static String expectedCSVHeader() {
        return "ID;CREATION_DATE;NAME;PARTS;SINGLES;X;Y;EST_DATE;GENRE;ST_NAME;ST_ADDR";
    }

    public static Optional<MusicBand> createFromCSVRow(String line) {
        String[] tokens = line.split(";", 11);
        assert tokens.length == 11;

        try {
            Long id = Long.valueOf(tokens[0]);

            LocalDateTime creationCDT = LocalDateTime.parse(tokens[1]);

            String name = tokens[2];
            if (name.isEmpty()) throw new IllegalArgumentException();

            //noinspection WrapperTypeMayBePrimitive
            Long participants = Long.valueOf(tokens[3]);
            if (participants <= 0) throw new NumberFormatException();

            int singles = Integer.parseInt(tokens[4]);
            if (singles <= 0) throw new NumberFormatException();

            long x = Long.parseLong(tokens[5]);

            Integer y = Integer.parseInt(tokens[6]);

            ZonedDateTime estCDT = ZonedDateTime.parse(tokens[7]);

            MusicGenre genre = (tokens[8].isEmpty()) ? null : MusicGenre.valueOf(tokens[8]);

            String stName = tokens[9].isEmpty() ? null : tokens[9];
            String stAddress = tokens[10].isEmpty() ? null : tokens[10];
            Studio studio = (stName != null || stAddress != null) ? new Studio() : null;
            if (studio != null) studio.setName(stName).setAddress(stAddress);

            MusicBandBuilder builder = new MusicBandBuilder(
                    name,
                    new Coordinates(x, y),
                    participants,
                    singles,
                    estCDT
            ).setGenre(genre).setStudio(studio);
            MusicBand element = new MusicBand(builder);
            element.setId(id);
            element.setCreationDate(creationCDT);
            return Optional.of(element);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static String toCSVRow(MusicBand e) {
        String id = e.getId().toString();
        String creationCDT = e.creationDate.toString();

        String name = e.name;

        String x = String.valueOf(e.coordinates.x);
        String y = e.coordinates.y.toString();

        String participants = e.numberOfParticipants.toString();
        String singles = String.valueOf(e.singlesCount);

        String estCDT = e.establishmentDate.toString();

        String genre = e.genre == null ? "" : e.genre.name();

        String stName = "";
        String stAddress = "";
        if (e.studio != null) {
            if (e.studio.name != null) stName = e.studio.name;
            if (e.studio.address != null) stAddress = e.studio.address;
        }
        return id + ";" +
                creationCDT + ";" +
                name + ";" +
                participants + ";" +
                singles + ";" +
                x + ";" +
                y + ";" +
                estCDT + ";" +
                genre + ";" +
                stName + ";" +
                stAddress + ";" +
                "\n";
    }
}
