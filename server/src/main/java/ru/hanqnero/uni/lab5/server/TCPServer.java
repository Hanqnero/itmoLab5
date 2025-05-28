package ru.hanqnero.uni.lab5.server;

import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.util.NetworkConfig;
import ru.hanqnero.uni.lab5.util.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Non-blocking TCP server implementation for handling client connections.
 * 
 * <p>This server uses Java NIO (New I/O) with a Selector to handle multiple
 * client connections concurrently without blocking threads. Each client
 * connection maintains its own state for handling partial reads and writes.</p>
 * 
 * <p>The server supports serialization and deserialization of Command and
 * ExecutionResult objects for communication with clients. It handles
 * connection acceptance, reading commands from clients, and writing
 * responses back to clients.</p>
 * 
 * <p>Key features:</p>
 * <ul>
 *   <li>Non-blocking I/O operations</li>
 *   <li>Concurrent client connection handling</li>
 *   <li>Partial read/write support for large messages</li>
 *   <li>Automatic client state management and cleanup</li>
 * </ul>
 * 
 * @author hanqnero
 * @version 1.0
 */
public class TCPServer {
    public final static String HOSTNAME = NetworkConfig.HOSTNAME;
    public final static int PORT = NetworkConfig.PORT;

    public final static int BUF_SIZE = NetworkConfig.BUF_SIZE;
    
    private ServerSocketChannel ssc;
    private Selector selector;
    
    // Track partial reads/writes for each client connection
    private final Map<SocketChannel, ClientState> clientStates = new ConcurrentHashMap<>();
    
    /**
     * Client state for handling partial I/O operations.
     * 
     * <p>Each client connection has its own state object to track
     * read and write buffers, pending responses, and operation status.
     * This enables the server to handle partial reads and writes
     * across multiple selector iterations.</p>
     */
    private static class ClientState {
        final ByteBuffer readBuffer = ByteBuffer.allocate(BUF_SIZE);
        final ByteBuffer writeBuffer = ByteBuffer.allocate(BUF_SIZE);
        ExecutionResult pendingResponse = null;
        boolean writeInProgress = false;
        
        void reset() {
            readBuffer.clear();
            writeBuffer.clear();
            pendingResponse = null;
            writeInProgress = false;
        }
    }

    /**
     * Initializes the TCP server.
     * 
     * <p>Opens a server socket channel, configures it for non-blocking mode,
     * binds it to the configured hostname and port, and registers it with
     * a selector for accepting incoming connections.</p>
     * 
     * @throws IOException if an I/O error occurs during initialization
     */
    public void init() throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        var inetAddr = new InetSocketAddress(HOSTNAME, PORT);
        ssc.bind(inetAddr);

        selector = SelectorProvider.provider().openSelector();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * Performs a non-blocking select operation and returns an iterator over ready keys.
     * 
     * <p>Uses selectNow() to check for ready channels without blocking.
     * Returns an iterator over the selection keys that are ready for I/O operations.</p>
     * 
     * @return iterator over ready SelectionKey objects
     * @throws IOException if an I/O error occurs during selection
     */
    public Iterator<SelectionKey> selectNowIter() throws IOException {
        selector.selectNow();
        return selector.selectedKeys().iterator();
    }

    /**
     * Handles a ready SelectionKey based on its ready operations.
     * 
     * <p>Dispatches to appropriate handler methods based on whether the key
     * is ready for accept, read, or write operations. Only processes valid keys.</p>
     * 
     * @param key the SelectionKey to handle
     * @param executor function to execute commands and produce results
     * @throws IOException if an I/O error occurs during key handling
     */
    public void handleKey(SelectionKey key, Function<Command, ExecutionResult> executor) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                accept(key);
            } else if (key.isReadable()) {
                read(key, executor);
            } else if (key.isWritable()) {
                write(key);
            }
        }
    }

    /**
     * Accepts a new client connection.
     * 
     * <p>Accepts an incoming connection from the server socket channel,
     * configures it for non-blocking mode, creates a new client state,
     * and registers it for read operations.</p>
     * 
     * @param key the SelectionKey for the server socket channel
     */
    public void accept(SelectionKey key) {
        try (ServerSocketChannel ssc = (ServerSocketChannel) key.channel()) {
            SocketChannel socketChannel;
            socketChannel = ssc.accept();

            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                System.out.println("Accepted a connection from client on address " + socketChannel.getRemoteAddress());
                
                // Initialize client state
                clientStates.put(socketChannel, new ClientState());
                
                // Register for read operations
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
        } catch (IOException e) {
            System.out.println("Could not accept connection from a client: " + e.getMessage());
        }
    }

    /**
     * Reads data from a client connection and processes commands.
     * 
     * <p>Attempts to read data from the client into the read buffer.
     * When a complete command is received, deserializes it, executes it
     * using the provided executor function, and prepares the response
     * for writing back to the client.</p>
     * 
     * <p>Handles partial reads by maintaining state across multiple
     * selector iterations. Automatically cleans up disconnected clients.</p>
     * 
     * @param key the SelectionKey for the client socket channel
     * @param executor function to execute commands and produce results
     * @throws IOException if an I/O error occurs during reading
     */
    public void read(SelectionKey key, Function<Command, ExecutionResult> executor) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ClientState clientState = clientStates.get(socketChannel);
        
        if (clientState == null) {
            // Client not found, close connection
            key.cancel();
            socketChannel.close();
            return;
        }
        
        int bytesRead;
        try {
            bytesRead = socketChannel.read(clientState.readBuffer);
        } catch (IOException e) {
            // Connection error, cleanup and close
            cleanupClient(key, socketChannel);
            return;
        }
        
        if (bytesRead == -1) {
            // Client disconnected
            cleanupClient(key, socketChannel);
            return;
        }
        
        if (bytesRead == 0) {
            // No data available right now
            return;
        }
        
        System.out.println("Read " + bytesRead + " bytes from client");
        
        // Try to deserialize a complete command
        try {
            clientState.readBuffer.flip();
            Command command = Serializer.deserialize(clientState.readBuffer);
            
            System.out.println("Deserialized command: " + command.toString());
            

            // Prepare for writing response
            clientState.pendingResponse = executor.apply(command); // Execute command
            clientState.readBuffer.clear(); // Reset for next command
            
            // Switch to write mode
            key.interestOps(SelectionKey.OP_WRITE);
            
        } catch (Exception e) {
            // Not enough data for complete command yet, compact buffer and wait for more
            clientState.readBuffer.compact();
            
            // Check if buffer is full but still can't deserialize - this indicates a problem
            if (!clientState.readBuffer.hasRemaining()) {
                System.out.println("Buffer full but cannot deserialize command, closing connection");
                cleanupClient(key, socketChannel);
            }
        }
    }
    
    private void cleanupClient(SelectionKey key, SocketChannel socketChannel) {
        try {
            clientStates.remove(socketChannel);
            key.cancel();
            socketChannel.close();
            System.out.println("Cleaned up client connection");
        } catch (IOException e) {
            System.out.println("Error during client cleanup: " + e.getMessage());
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ClientState clientState = clientStates.get(socketChannel);
        
        if (clientState == null || clientState.pendingResponse == null) {
            // No response to write, switch back to read mode
            key.interestOps(SelectionKey.OP_READ);
            return;
        }
        
        // Initialize write buffer if not already done
        if (!clientState.writeInProgress) {
            clientState.writeBuffer.clear();
            ByteBuffer serializedResponse = Serializer.serialize(clientState.pendingResponse);
            
            // Copy serialized data to write buffer
            serializedResponse.flip();
            clientState.writeBuffer.put(serializedResponse);
            clientState.writeBuffer.flip();
            clientState.writeInProgress = true;
            
            System.out.println("Starting to write response: " + clientState.pendingResponse.toString());
        }
        
        try {
            int bytesWritten = socketChannel.write(clientState.writeBuffer);
            System.out.println("Wrote " + bytesWritten + " bytes to client");
            
            if (!clientState.writeBuffer.hasRemaining()) {
                // All data written successfully
                System.out.println("Response written completely");
                
                // Reset client state for next command
                clientState.reset();
                
                // Switch back to read mode
                key.interestOps(SelectionKey.OP_READ);
            }
            // If writeBuffer still has remaining data, keep OP_WRITE set and continue on next iteration
            
        } catch (IOException e) {
            System.out.println("Error writing to client: " + e.getMessage());
            cleanupClient(key, socketChannel);
        }
    }

    public void close() throws IOException {
        // Clean up all client connections
        for (SocketChannel client : clientStates.keySet()) {
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("Error closing client connection: " + e.getMessage());
            }
        }
        clientStates.clear();
        
        if (selector != null && selector.isOpen()) {
            selector.close();
        }
        if (ssc != null && ssc.isOpen()) {
            ssc.close();
        }
    }
}
