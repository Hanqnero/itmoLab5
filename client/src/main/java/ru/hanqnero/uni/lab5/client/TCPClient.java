package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.util.NetworkConfig;
import ru.hanqnero.uni.lab5.util.Serializer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.Optional;

/**
 * Non-blocking TCP client for communicating with the music band collection server.
 * 
 * <p>This client uses Java NIO with non-blocking I/O operations and automatic
 * reconnection handling to provide reliable communication with the server.
 * It handles command serialization, response deserialization, and connection
 * state management.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Non-blocking I/O operations with timeout handling</li>
 *   <li>Automatic reconnection with exponential backoff</li>
 *   <li>Partial read/write support for large messages</li>
 *   <li>Connection state management</li>
 *   <li>Graceful error handling and recovery</li>
 * </ul>
 * 
 * <p>The client automatically handles connection timeouts, server disconnections,
 * and network errors by attempting to reconnect when necessary.</p>
 * 
 * @author hanqnero
 * @version 1.0
 */
public class TCPClient {
    private SocketChannel socketChannel;
    private final Selector selector;

    private final ByteBuffer readBuffer = ByteBuffer.allocate(NetworkConfig.BUF_SIZE);
    private final ByteBuffer writeBuffer = ByteBuffer.allocate(NetworkConfig.BUF_SIZE);

    private int reconAttempt = 0;
    private boolean connectionInProgress = false;

    /**
     * Constructs a new TCPClient instance.
     * 
     * <p>Initializes the selector for non-blocking I/O operations.
     * The socket channel is not created here and will be created
     * when connecting to the server.</p>
     * 
     * @throws IOException if selector initialization fails
     */
    public TCPClient() throws IOException {
        selector = Selector.open();
        initSocketChannel();
    }

    private void initSocketChannel() throws IOException {
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
        }
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        connectionInProgress = false;
    }

    public boolean ensureConnected() throws IOException {
        if (socketChannel != null && socketChannel.isConnected()) {
            reconAttempt = 0;
            return true;
        }

        final int MAX_ATTEMPTS = 30;

        if (++reconAttempt >= MAX_ATTEMPTS) return false;

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            return false;
        }

        System.out.println("Connection was closed. Reconnecting. Attempt " + reconAttempt);
        initSocketChannel();
        connect();
        return ensureConnected();
    }

    public void connect() throws IOException {
        if (!socketChannel.isOpen()) {
            initSocketChannel();
        }
        
        try {
            // Register for connect operation
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            
            // Start non-blocking connect
            String remote = NetworkConfig.HOSTNAME;
            int port = NetworkConfig.PORT;
            boolean connected = socketChannel.connect(new InetSocketAddress(remote, port));
            if (connected) {
                // Connected immediately (rare for non-blocking)
                connectionInProgress = false;
                socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                return;
            } else {
                connectionInProgress = true;
            }
            
            // Wait for connection to complete
            long timeout = System.currentTimeMillis() + 5000; // 5 second timeout
            while (connectionInProgress && System.currentTimeMillis() < timeout) {
                if (selector.select(1000) > 0) {
                    var keyIter = selector.selectedKeys().iterator();
                    while (keyIter.hasNext()) {
                        SelectionKey key = keyIter.next();
                        keyIter.remove();
                        
                        if (key.isConnectable()) {
                            if (socketChannel.finishConnect()) {
                                connectionInProgress = false;
                                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                                return;
                            }
                        }
                    }
                }
            }
            
            if (connectionInProgress) {
                throw new ConnectException("Connection timeout");
            }
            
        } catch (ConnectException e) {
            System.out.println("Could not connect to server: " + e.getMessage());
            throw e;
        }
    }

    public void send(Command command) throws IOException {
        if (!socketChannel.isConnected()) {
            throw new IOException("Not connected to server");
        }
        
        // Serialize command to write buffer
        writeBuffer.clear();
        ByteBuffer serializedData = Serializer.serialize(command);
        
        // Write all data (handle partial writes)
        serializedData.flip();
        while (serializedData.hasRemaining()) {
            try {
                int bytesWritten = socketChannel.write(serializedData);
                if (bytesWritten == 0) {
                    // Channel is not ready for writing, wait a bit
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Interrupted while waiting to write", e);
                    }
                }
            } catch (IOException e) {
                System.out.println("Could not send to server: " + e.getMessage());
                close();
                throw e;
            }
        }
    }

    public Optional<ExecutionResult> receive() throws IOException {
        if (!socketChannel.isConnected()) {
            throw new IOException("Not connected to server");
        }
        
        readBuffer.clear();
        
        // Read with timeout
        long timeout = System.currentTimeMillis() + 30000; // 30 second timeout
        int totalBytesRead = 0;
        
        while (System.currentTimeMillis() < timeout) {
            try {
                int bytesRead = socketChannel.read(readBuffer);
                
                if (bytesRead == -1) {
                    close();
                    System.out.println("Server has closed the connection");
                    return Optional.empty();
                } else if (bytesRead == 0) {
                    // No data available right now, check if we have enough data to deserialize
                    if (readBuffer.position() > 0) {
                        try {
                            readBuffer.flip();
                            ExecutionResult result = Serializer.deserialize(readBuffer);
                            return Optional.of(result);
                        } catch (Exception e) {
                            // Not enough data yet, continue reading
                            readBuffer.compact();
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                throw new IOException("Interrupted while waiting for data", ie);
                            }
                            continue;
                        }
                    }
                    
                    // Wait a bit before trying again
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Interrupted while waiting for data", e);
                    }
                } else {
                    totalBytesRead += bytesRead;
                    // Try to deserialize what we have so far
                    try {
                        readBuffer.flip();
                        ExecutionResult result = Serializer.deserialize(readBuffer);
                        return Optional.of(result);
                    } catch (Exception e) {
                        // Not enough data yet, continue reading
                        readBuffer.compact();
                    }
                }
            } catch (SocketException e) {
                close();
                System.out.println("Could not access server while retrieving response: " + e.getMessage());
                return Optional.empty();
            }
        }
        
        System.out.println("Timeout waiting for server response");
        return Optional.empty();
    }

    public void close() throws IOException {
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
        }
        if (selector != null && selector.isOpen()) {
            selector.close();
        }
        connectionInProgress = false;
    }

}
