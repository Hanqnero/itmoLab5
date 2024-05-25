package ru.hanqnero.uni.lab5.util;

import java.io.*;
import java.nio.ByteBuffer;

public abstract class Serializer {
    public static ByteBuffer serialize (Serializable obj) throws IOException {
        var baos = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        ByteBuffer buffer = ByteBuffer.allocate(baos.size());
        buffer.put(baos.toByteArray());
        return buffer;
    }

    public static <T extends Serializable> T deserialize (ByteBuffer buffer) throws IOException {
        try (var bais = new ByteArrayInputStream(buffer.array())) {
            var ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
