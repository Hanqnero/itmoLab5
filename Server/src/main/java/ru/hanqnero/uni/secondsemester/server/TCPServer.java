package ru.hanqnero.uni.secondsemester.server;

import ru.hanqnero.uni.secondsemester.commons.contract.commands.Command;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;
import ru.hanqnero.uni.secondsemester.commons.util.Serializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.function.Function;

public class TCPServer {
    public final static String HOSTNAME = "localhost";
    public final static int PORT = 16484;

    public final static int BUF_SIZE = 4096;
    private final ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);

    private ServerSocketChannel ssc;
    private Selector selector;


    public void init() throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        var inetAddr = new InetSocketAddress(HOSTNAME, PORT);
        ssc.bind(inetAddr);

        selector = SelectorProvider.provider().openSelector();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    public Iterator<SelectionKey> selectNowIter() throws IOException {
        selector.selectNow();
        return selector.selectedKeys().iterator();
    }

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

    public void accept(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            System.out.println("Accepted a connection from client on address " + socketChannel.getLocalAddress());
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("Could not accept connection from a client.");
        }
    }

    public void read(SelectionKey key, Function<Command, ExecutionResult> executor) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        this.buffer.clear();
        int bytesRead;
        try {
            bytesRead = socketChannel.read(this.buffer);
        } catch (IOException e) {
            key.cancel();
            socketChannel.close();
            return;
        }
        if (bytesRead == -1) {
            key.cancel();
            return;
        }
        System.out.println("Got buffer from client");
        System.out.println(buffer);

        Command command = Serializer.deserialize(buffer);
        ExecutionResult response = executor.apply(command);
        System.out.println("Deserialized command from buffer " + command.toString());
        socketChannel.register(this.selector, SelectionKey.OP_WRITE, response);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ExecutionResult response = (ExecutionResult) key.attachment();
        System.out.println("Starting serializing response " + response.toString());

        ByteBuffer writeBuffer = Serializer.serialize(response);
        writeBuffer.flip();
        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public void close() throws IOException {
        if (ssc != null) {
            ssc.close();
        }
    }
}
