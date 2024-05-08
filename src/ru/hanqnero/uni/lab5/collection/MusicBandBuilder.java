package ru.hanqnero.uni.lab5.collection;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

// Паттерн builder
public class MusicBandBuilder {
    final String name;
    final Coordinates  coords;
    final Long numberOfParticipants;
    final int singlesCount;
    final ZonedDateTime establishmentDate;

    Long id;
    LocalDateTime creationDate;
    MusicGenre genre;
    Studio studio;
    public MusicBandBuilder(
            String name,
            Coordinates coords,
            Long numberOfParticipants,
            int singlesCount,
            ZonedDateTime establishmentDate
    ) {
        this.name = name;
        this.coords = coords;
        this.numberOfParticipants = numberOfParticipants;
        this.singlesCount = singlesCount;
        this.establishmentDate = establishmentDate;
    }

    public MusicBandBuilder setGenre(MusicGenre genre) {
        this.genre = genre;
        return this;
    }

    public MusicBandBuilder setStudio(Studio studio) {
        this.studio = studio;
        return this;
    }

    public MusicBand build() {
        return new MusicBand(this);
    }
}
