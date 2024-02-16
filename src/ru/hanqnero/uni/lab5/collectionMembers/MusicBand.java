package ru.hanqnero.uni.lab5.collectionMembers;

public class MusicBand {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long numberOfParticipants; //Поле не может быть null, Значение поля должно быть больше 0
    private int singlesCount; //Значение поля должно быть больше 0
    private java.time.ZonedDateTime establishmentDate; //Поле не может быть null
    private MusicGenre genre; //Поле может быть null
    private Studio studio; //Поле может быть null
}

