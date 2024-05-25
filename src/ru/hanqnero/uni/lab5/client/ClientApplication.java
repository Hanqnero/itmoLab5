package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.util.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Optional;

public class ClientApplication {
    private final ConsoleManager console;
    private final Map<String, CommandFactory> factories;
    private final Map<String, ExecutionResultHandler> handlers;
    private SocketChannel socketChannel;


    public ClientApplication() {
        console = new ConsoleManager(System.in, System.out);
        this.factories = CommandInfo.createFactoriesView();
        this.handlers = CommandInfo.createHandlersView();
    }

    public static void main(String[] args) {
        ClientApplication app = new ClientApplication();
        try {
            app.initSocketChannel();
        } catch (IOException e) {
            app.console.printlnErr("Could not initialize network stack. Fatal Error.");
            System.exit(1);
        }
        try {
            app.console.println("Connecting to server...");
            if (!app.connect("127.0.0.1", 16482)) {
                app.console.printlnWarn("Connecting to server will take some time");
            }
            while (!app.socketChannel.finishConnect()) {
                // Thread.sleep(100);
            }
            app.console.printlnSuc("Connection successful.");
        } catch (IOException e) {
            app.console.printlnErr("Could not connect to server. Fatal Error.");
            System.exit(1);
        }
        app.repl();
        app.stop();
    }

    private Optional<Command> createCommandFromTokens(String[] tokens) throws SubtypeScanError {

        var factory = this.factories.get(tokens[0]);
        if (factory == null) {
            return Optional.empty();
        }

        factory.setConsole(console);
        return Optional.of(factory.createCommand(tokens));
    }

    public Optional<Command> readCommand() {
        String line = "";
        try {
           line = console.nextLine();
        } catch (ConsoleEmptyException e) {
            console.printlnErr("Reached EOF");
        }

        if (line.isEmpty()) {
            return Optional.empty();
        }

        String[] tokens = line.split(" ");

        assert tokens.length > 0;

        Optional<Command> commandOptional;
        try {
            commandOptional = createCommandFromTokens(tokens);
        } catch (SubtypeScanError e) {
            return Optional.empty();
        }
        if (commandOptional.isEmpty()) {
            console.printlnErr("No such command `%s`".formatted(tokens[0]));
        }
        return commandOptional;
    }

    public void handleResponse(ExecutionResult response) {
        if (!(handlers.containsKey(response.getCommandName()))) {
            console.printlnErr("Cannot handle received result for command `%s`".formatted(response.getCommandName()));
            return;
        }
        ExecutionResultHandler handler = handlers.get(response.getCommandName());
        handler.setConsole(console);
        handler.setClient(this);
        handler.handleResult(response);
    }

    public void repl() {
        while (console.hasNext()) {
            Optional<Command> command = readCommand();
            boolean commandSent;
            if (command.isPresent()) {
                commandSent = sendCommand(command.get());
            } else {
                continue;
            }
            if (commandSent) {
                ExecutionResult receivedResult = retrieveResult();
                handleResponse(receivedResult);
            }
        }
        console.printlnWarn("EOF reached. exiting REPL...");
    }

    public void initSocketChannel() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel
                .bind(null)
                .configureBlocking(false);
    }

    public boolean connect(String hostname, int port) throws IOException {
        return socketChannel.connect(new InetSocketAddress(hostname, port));
    }

    public boolean sendCommand(Command command) {
        try {
            var baos = new ByteArrayOutputStream();
            var oos = new ObjectOutputStream(baos);

            oos.writeObject(command);
            oos.flush();
            oos.close();

            var buf = ByteBuffer.wrap(baos.toByteArray());
            socketChannel.write(buf);
            return true;
        } catch (IOException e) {
            console.printlnErr("Could not send command");
            return false;
        }
    }

    public ExecutionResult retrieveResult() {
        try {
            var buf = ByteBuffer.allocate(2048);
            socketChannel.read(buf);
            buf.flip();
            var bis = new ByteArrayInputStream(buf.array());
            var ois = new ObjectInputStream(bis);

            return (ExecutionResult) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            console.printlnErr("Error reading result from server");
            console.printlnErr(e.getMessage());
            return null;
        }
    }

    public void closeConnection() throws IOException {
        socketChannel.close();
    }

    public void stop() {
        try {
            closeConnection();
            console.println("Connection closed successfully.");
        } catch (IOException e) {
            console.printlnErr("Could not close connection. Exiting anyway");
        }
    }
}
