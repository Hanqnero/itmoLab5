package ru.hanqnero.uni.lab5.collection;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Builder pattern implementation for creating MusicBand objects.
 * <p>
 * This class implements the Builder design pattern to provide a flexible and
 * readable way to construct MusicBand instances. It separates the construction
 * process from the representation, allowing step-by-step creation of complex
 * objects with optional parameters.
 * </p>
 * 
 * <p>
 * The builder enforces the required parameters through the constructor while
 * allowing optional parameters (genre and studio) to be set through fluent
 * method chaining. This approach ensures that all mandatory fields are provided
 * while maintaining flexibility for optional components.
 * </p>
 * 
 * <p>
 * Usage example:
 * <pre>
 * MusicBandBuilder builder = new MusicBandBuilder(
 *     "The Beatles", coordinates, 4L, 20, establishmentDate)
 *     .setGenre(MusicGenre.ROCK)
 *     .setStudio(studio);
 * MusicBand band = new MusicBand(builder);
 * </pre>
 * </p>
 * 
 * <p>
 * Thread safety: This class is not thread-safe and should not be shared
 * between multiple threads without external synchronization.
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 * @see MusicBand
 * @see Coordinates
 * @see MusicGenre
 * @see Studio
 */
// Паттерн builder
public class MusicBandBuilder implements Serializable {
    /** The name of the music band (required, cannot be null or empty) */
    final String name;
    /** The coordinates where the band is located (required, cannot be null) */
    final Coordinates  coords;
    /** The number of participants in the band (required, must be greater than 0) */
    final Long numberOfParticipants;
    /** The number of singles released by the band (required, must be greater than 0) */
    final int singlesCount;
    /** The date when the band was established (required, cannot be null) */
    final ZonedDateTime establishmentDate;

    /** The musical genre of the band (optional, can be null) */
    MusicGenre genre;
    /** The recording studio associated with the band (optional, can be null) */
    Studio studio;

    /**
     * Constructs a new MusicBandBuilder with required parameters.
     * <p>
     * This constructor initializes the builder with all mandatory fields
     * that must be provided for any MusicBand. Optional fields (genre and studio)
     * can be set later using the fluent setter methods.
     * </p>
     * 
     * @param name the name of the music band, must not be null or empty
     * @param coords the coordinates where the band is located, must not be null
     * @param numberOfParticipants the number of band members, must be greater than 0
     * @param singlesCount the number of singles released, must be greater than 0
     * @param establishmentDate the date when the band was established, must not be null
     * @throws IllegalArgumentException if any parameter violates its constraints
     */
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

    /**
     * Sets the musical genre for the band.
     * <p>
     * This is an optional parameter that can be used to specify the musical
     * style or category of the band. The method returns the builder instance
     * to support method chaining.
     * </p>
     * 
     * @param genre the musical genre, can be null
     * @return this builder instance for method chaining
     */
    public MusicBandBuilder setGenre(MusicGenre genre) {
        this.genre = genre;
        return this;
    }

    /**
     * Sets the recording studio for the band.
     * <p>
     * This is an optional parameter that can be used to specify the studio
     * where the band records their music. The method returns the builder instance
     * to support method chaining.
     * </p>
     * 
     * @param studio the recording studio, can be null
     * @return this builder instance for method chaining
     */
    public MusicBandBuilder setStudio(Studio studio) {
        this.studio = studio;
        return this;
    }

    /**
     * Gets the number of singles count for the band.
     * 
     * @return the number of singles released by the band
     */
    public int getSinglesCount() {
        return singlesCount;
    }

    /**
     * Gets the name of the band.
     * 
     * @return the band name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the coordinates of the band's location.
     * 
     * @return the coordinates object
     */
    public Coordinates getCoords() {
        return coords;
    }

    /**
     * Gets the number of participants in the band.
     * 
     * @return the number of band members
     */
    public Long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    /**
     * Gets the establishment date of the band.
     * 
     * @return the date when the band was established
     */
    public ZonedDateTime getEstablishmentDate() {
        return establishmentDate;
    }

    /**
     * Gets the musical genre of the band.
     * 
     * @return the musical genre, may be null
     */
    public MusicGenre getGenre() {
        return genre;
    }

    /**
     * Gets the recording studio associated with the band.
     * 
     * @return the studio object, may be null
     */
    public Studio getStudio() {
        return studio;
    }
}
