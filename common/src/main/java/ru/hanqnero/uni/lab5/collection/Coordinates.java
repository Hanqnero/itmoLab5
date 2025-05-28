package ru.hanqnero.uni.lab5.collection;

import java.io.Serializable;

/**
 * Represents a pair of coordinates (x, y) for positioning in a 2D space.
 * <p>
 * This immutable class encapsulates a coordinate point with a long x-value
 * and an Integer y-value. The coordinates are used to specify the location
 * of music bands in the collection system. The class ensures that the y
 * coordinate cannot be null while allowing the x coordinate to have the
 * full range of long values.
 * </p>
 * 
 * <p>
 * Key characteristics:
 * <ul>
 *   <li>Immutable - coordinates cannot be changed after creation</li>
 *   <li>Serializable - can be transmitted over network or stored</li>
 *   <li>Thread-safe - immutability ensures thread safety</li>
 *   <li>Validation - ensures y coordinate is not null</li>
 * </ul>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 */
public class Coordinates implements Serializable {
    /** The x-coordinate value (can be any long value) */
    protected final long x;
    /** The y-coordinate value (cannot be null) */
    protected final Integer y; //Поле не может быть null

    /**
     * Constructs a new Coordinates object with the specified x and y values.
     * <p>
     * Creates an immutable coordinate point with the given x and y values.
     * The y parameter must not be null as per the business rules of the
     * music band collection system.
     * </p>
     * 
     * @param x the x-coordinate value (any long value is allowed)
     * @param y the y-coordinate value (must not be null)
     * @throws IllegalArgumentException if y is null
     */
    public Coordinates(long x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate value.
     * 
     * @return the x-coordinate as a long
     */
    public long getX() {
        return x;
    }

    /**
     * Returns the y-coordinate value.
     * 
     * @return the y-coordinate as an Integer
     */
    public Integer getY() {
        return y;
    }

    /**
     * Returns a string representation of the coordinates.
     * <p>
     * The string format is "Coordinates{x=value, y=value}" which provides
     * a clear and readable representation of the coordinate values.
     * </p>
     * 
     * @return a string representation of this coordinate object
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
