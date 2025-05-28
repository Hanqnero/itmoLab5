package ru.hanqnero.uni.lab5.collection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a music band with all its properties and metadata.
 * 
 * <p>This class encapsulates all information about a music band including
 * basic details (name, coordinates, participants), temporal information
 * (creation and establishment dates), and optional attributes (genre, studio).</p>
 * 
 * <p>MusicBand objects are immutable once created (except for ID and creation date)
 * and implement Comparable for natural ordering based on band name.
 * The class uses the Builder pattern for construction to ensure proper
 * validation and initialization.</p>
 * 
 * <p>Key constraints:</p>
 * <ul>
 *   <li>ID must be unique and positive (auto-generated)</li>
 *   <li>Name cannot be null or empty</li>
 *   <li>Coordinates and numberOfParticipants cannot be null</li>
 *   <li>numberOfParticipants and singlesCount must be positive</li>
 *   <li>establishmentDate cannot be null</li>
 *   <li>genre and studio are optional (can be null)</li>
 * </ul>
 * 
 * @author hanqnero
 * @version 1.0
 */
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

    /**
     * Constructs a MusicBand using the Builder pattern.
     * 
     * <p>This constructor initializes all final fields from the builder
     * and ensures proper validation. The ID and creation date are set
     * separately after construction.</p>
     * 
     * @param builder the MusicBandBuilder containing all band properties
     */
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

    /**
     * Sets the unique identifier for this music band.
     * 
     * <p>The ID should be unique across all music bands and positive.
     * This method is typically called by the collection manager when
     * adding the band to the collection.</p>
     * 
     * @param id the unique identifier for this band
     */
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

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public int getSinglesCount() {
        return singlesCount;
    }

    public java.time.ZonedDateTime getEstablishmentDate() {
        return establishmentDate;
    }

    public MusicGenre getGenre() {
        return genre;
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