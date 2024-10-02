package ru.hanqnero.uni.secondsemester.client;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.util.Serializer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Optional;

public class TCPClient {
    private SocketChannel socketChannel;
    private final String REMOTE = "127.0.0.1";
    private final int PORT = 2227;
    private final int BUF_SIZE = 2048;

    private final ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);

    private int reconAttempt = 0;


    public TCPClient() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
    }

    public boolean ensureConnected() throws IOException {
        if (socketChannel.isOpen()) {
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
        socketChannel = SocketChannel.open();
        connect();
        return ensureConnected();
    }

    public void connect() throws IOException {
        try {
            socketChannel.connect(new InetSocketAddress(REMOTE, PORT));
        } catch (ConnectException e) {
            System.out.println("Could not connect to server");
        }
    }

    public void send(Command command) throws IOException {
        ByteBuffer writeBuffer = Serializer.serialize(command);
        writeBuffer.flip();
        try {
            socketChannel.write(writeBuffer);
        } catch (IOException e) {
            System.out.println("Could not send to server");
            close();
        }
    }

    public Optional<ExecutionResult> receive() throws IOException  {
        buffer.clear();
        int n;
        try {
            n = socketChannel.read(buffer);
        } catch (SocketException e) {
            close();
            System.out.println("Could not access server while retrieving response");
            return Optional.empty();
        }
        if (n == -1) {
            close();
            System.out.println("Server has closed the connection");
            return Optional.empty();
        }

        return Optional.of(Serializer.deserialize(buffer));
    }

    public void close() throws IOException {
        socketChannel.close();
    }

}
