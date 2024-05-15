package ru.hanqnero.uni.lab5.collection;

import java.time.LocalDateTime;

public class MusicBand implements Comparable<MusicBand> {
    protected Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    protected java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    protected String name; //Поле не может быть null, Строка не может быть пустой
    protected Coordinates coordinates; //Поле не может быть null
    protected Long numberOfParticipants; //Поле не может быть null, Значение поля должно быть больше 0
    protected int singlesCount; //Значение поля должно быть больше 0
    protected java.time.ZonedDateTime establishmentDate; //Поле не может быть null
    protected MusicGenre genre; //Поле может быть null
    protected Studio studio; //Поле может быть null
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
        return this.numberOfParticipants.compareTo(o.numberOfParticipants);
    }
}
