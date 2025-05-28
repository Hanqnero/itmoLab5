package ru.hanqnero.uni.lab5.util;

/**
 * Network configuration constants shared between client and server.
 */
public final class NetworkConfig {
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 16484;
    public static final int BUF_SIZE = 4096;
    
    private NetworkConfig() {
        // Utility class, prevent instantiation
    }
}
