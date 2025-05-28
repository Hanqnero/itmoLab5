package ru.hanqnero.uni.lab5.util;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Utility class for serializing and deserializing objects for network communication.
 * 
 * <p>This class provides static methods for converting Serializable objects to
 * ByteBuffer format for network transmission and vice versa. It is used by both
 * the client and server for encoding commands and execution results.</p>
 * 
 * <p>The serialization format uses Java's built-in object serialization mechanism
 * wrapped in ByteBuffer for efficient NIO operations. All objects must implement
 * the Serializable interface to be processed by this class.</p>
 * 
 * <p>Thread Safety: This class is thread-safe as it only contains static methods
 * and creates new streams for each operation.</p>
 * 
 * @author hanqnero
 * @version 1.0
 */
public abstract class Serializer {
    /**
     * Serializes a Serializable object into a ByteBuffer.
     * 
     * <p>Converts the given object into a byte array using Java object
     * serialization and wraps it in a ByteBuffer for network transmission.
     * The resulting buffer is ready for writing to a channel.</p>
     * 
     * @param obj the object to serialize (must implement Serializable)
     * @return a ByteBuffer containing the serialized object data
     * @throws IOException if an error occurs during serialization
     */
    public static ByteBuffer serialize (Serializable obj) throws IOException {
        var baos = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        ByteBuffer buffer = ByteBuffer.allocate(baos.size());
        buffer.put(baos.toByteArray());
        return buffer;
    }

    /**
     * Deserializes an object from a ByteBuffer.
     * 
     * <p>Converts byte data from a ByteBuffer back into a Java object using
     * object deserialization. The buffer should contain data that was previously
     * serialized using the {@link #serialize(Serializable)} method.</p>
     * 
     * @param <T> the type of object to deserialize
     * @param buffer the ByteBuffer containing serialized object data
     * @return the deserialized object
     * @throws IOException if an error occurs during deserialization
     * @throws RuntimeException if the class cannot be found during deserialization
     */
    public static <T extends Serializable> T deserialize (ByteBuffer buffer) throws IOException {
        try (var bais = new ByteArrayInputStream(buffer.array())) {
            var ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
