package ru.hanqnero.uni.lab5.commons.collection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MusicBand implements Comparable<MusicBand> {
    protected Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    protected java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    protected final String name; //Поле не может быть null, Строка не может быть пустой
    protected final Coordinates coordinates; //Поле не может быть null
    protected final Long numberOfParticipants; //Поле не может быть null, Значение поля должно быть больше 0
    protected final int singlesCount; //Значение поля должно быть больше 0
    protected final java.time.ZonedDateTime establishmentDate; //Поле не может быть null
    protected final MusicGenre genre; //Поле может быть null
    protected final Studio studio; //Поле может быть null
    protected boolean lockCreationDate = false;

    //Use Builder pattern
    public MusicBand(MusicBandBuilder builder) {
        this.name = builder.name;
        this.coordinates = builder.coords;
        this.numberOfParticipants = builder.numberOfParticipants;
        this.singlesCount = builder.singlesCount;
        this.establishmentDate = builder.establishmentDate;
        this.genre = builder.genre;
        this.studio = builder.studio;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        if (!lockCreationDate) {
            this.creationDate = creationDate;
            lockCreationDate = true;
        } else {
            System.err.println("Tried to update creation date of object which is already set.");
        }
    }

    @Override
    public int compareTo(MusicBand o) {
        return this.establishmentDate.compareTo(o.establishmentDate);
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Studio getStudio() {
        return studio;
    }

    @Override
    public String toString() {
        return "[%+024d]  ".formatted(id) +
                "Name: `%16s`  ".formatted(name) +
                "Members: %6d  ".formatted(numberOfParticipants) +
                "Singles: %6d  ".formatted(singlesCount) +
                "Established: %s  ".formatted(establishmentDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"))) +
                "Genre: %12s ".formatted(genre) +
                "at Studio: %16s".formatted(studio);
    }
}