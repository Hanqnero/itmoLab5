package ru.hanqnero.uni.lab5.collection;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a recording studio associated with a music band.
 * <p>
 * This class encapsulates information about a recording studio, including
 * its name and address. Both fields are optional and can be null, providing
 * flexibility for cases where complete studio information is not available.
 * The class uses a fluent interface pattern for setting properties.
 * </p>
 * 
 * <p>
 * Key features:
 * <ul>
 *   <li>Optional name and address fields (both can be null)</li>
 *   <li>Fluent interface for property setting</li>
 *   <li>Case-insensitive equality comparison</li>
 *   <li>Proper equals/hashCode implementation</li>
 *   <li>Serializable for network transmission</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Usage example:
 * <pre>
 * Studio studio = new Studio()
 *     .setName("Abbey Road Studios")
 *     .setAddress("London, UK");
 * </pre>
 * </p>
 * 
 * @author hanqnero
 * @version 1.0
 * @since 1.0
 */
public class Studio implements Serializable {
    /** The name of the recording studio (can be null) */
    protected String name; //Поле может быть null
    /** The address of the recording studio (can be null) */
    protected String address; //Поле может быть null
    
    /**
     * Default constructor that creates an empty studio.
     * <p>
     * Creates a new Studio instance with null name and address.
     * Properties can be set later using the fluent setter methods.
     * </p>
     */
    public Studio() {}

    /**
     * Sets the address of the studio using fluent interface.
     * <p>
     * This method allows chaining with other setter methods to configure
     * the studio properties in a readable manner.
     * </p>
     * 
     * @param address the address of the studio, can be null
     * @return this Studio instance for method chaining
     */
    public Studio setAddress(String address) {
        this.address = address;
        return this;
    }
    
    /**
     * Sets the name of the studio using fluent interface.
     * <p>
     * This method allows chaining with other setter methods to configure
     * the studio properties in a readable manner.
     * </p>
     * 
     * @param name the name of the studio, can be null
     * @return this Studio instance for method chaining
     */
    public Studio setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the name of the studio.
     * 
     * @return the studio name, can be null
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the address of the studio.
     * 
     * @return the studio address, can be null
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns a string representation of the studio.
     * <p>
     * The format is 'name@address' enclosed in single quotes,
     * providing a compact and readable representation.
     * </p>
     * 
     * @return a string representation of this studio
     */
    @Override
    public String toString() {
        return "'" + name + "@" + address + "'";
    }

    /**
     * Compares this studio with another object for equality.
     * <p>
     * Two studios are considered equal if they have the same name and address.
     * This method follows the general contract of Object.equals().
     * </p>
     * 
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Studio studio = (Studio) o;
        return Objects.equals(name, studio.name) && Objects.equals(address, studio.address);
    }

    /**
     * Returns a hash code value for this studio.
     * <p>
     * The hash code is computed based on the name and address fields,
     * consistent with the equals method implementation.
     * </p>
     * 
     * @return a hash code value for this studio
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    /**
     * Compares this studio with another studio for equality using case-insensitive comparison.
     * <p>
     * This method provides case-insensitive comparison of studio names and addresses,
     * which is useful for user-friendly matching operations.
     * </p>
     * 
     * @param o the studio to compare with
     * @return true if the studios are equal (case-insensitive), false otherwise
     */
    @SuppressWarnings("unused")
    public boolean equals(Studio o) {
        if (this == o) return true;
        if (o == null) return false;
        return this.address.equalsIgnoreCase(o.address) && this.name.equalsIgnoreCase(o.name);
    }
}
