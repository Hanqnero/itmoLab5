package ru.hanqnero.uni.lab5.collectionMembers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;

public class MusicBand {
    private final Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long numberOfParticipants; //Поле не может быть null, Значение поля должно быть больше 0
    private int singlesCount; //Значение поля должно быть больше 0
    private java.time.ZonedDateTime establishmentDate; //Поле не может быть null
    private MusicGenre genre; //Поле может быть null
    private Studio studio; //Поле может быть null

    public MusicBand(String name,
                     Coordinates coordinates,
                     Long numberOfParticipants,
                     int singlesCount,
                     ZonedDateTime establishmentDate,
                     MusicGenre genre,
                     Studio studio) {
        this.id = generateID();

        if (name == null) throw new NullPointerException("Field 'name' cannot be null");
        this.name = name;

        if (coordinates == null) throw new NullPointerException("Field 'coordinates' cannot be null");
        this.coordinates = coordinates;

        this.creationDate = LocalDateTime.now();

        if (numberOfParticipants == null) throw new NullPointerException("Field 'numberOfParticipants' cannot be null");
        if (numberOfParticipants.compareTo(0L) < 0) throw new IllegalArgumentException("Value of field 'numberOfParticipants' must be a positive number");
        this.numberOfParticipants = numberOfParticipants;

        if (singlesCount <= 0) throw new IllegalArgumentException("Value of field 'singlesCount' must be a positive number");
        this.singlesCount = singlesCount;

        if (establishmentDate == null) throw new NullPointerException("Field 'establishmentDate' cannot be null");
        this.establishmentDate = establishmentDate;

        this.genre = genre;
        this.studio = studio;
    }

    @Override
    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", singlesCount=" + singlesCount +
                ", establishmentDate=" + establishmentDate +
                ", genre=" + genre +
                ", studio=" + studio +
                '}';
    }

    private Long generateID() {
        return (long) UUID.randomUUID().hashCode();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public int getSinglesCount() {
        return singlesCount;
    }

    public void setSinglesCount(int singlesCount) {
        this.singlesCount = singlesCount;
    }

    public ZonedDateTime getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(ZonedDateTime establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }
}
