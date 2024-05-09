package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.contract.CommandInfo;
import ru.hanqnero.uni.lab5.contract.commands.Command;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.contract.results.ExecutionResult;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.ServerApplication;
import ru.hanqnero.uni.lab5.util.exceptions.ConsoleEmptyException;
import ru.hanqnero.uni.lab5.util.exceptions.SubtypeScanError;

import java.io.IOException;
import java.util.*;

public class ClientApplication {
    private final ConsoleManager console;
    private ServerApplication server;
    private final Map<String, CommandFactory> factories;
    private final Map<String, ExecutionResultHandler> handlers;


    public ClientApplication() {
        console = new ConsoleManager(System.in, System.out);
        this.factories = CommandInfo.createFactoriesView();
        this.handlers = CommandInfo.createHandlersView();
    }

    public void connect(ServerApplication server) {
        this.server = server;
    }

    private Optional<Command> createCommandFromTokens(String[] tokens) throws SubtypeScanError {

        var factory = this.factories.get(tokens[0]);
        if (factory == null) {
            return Optional.empty();
        }

        factory.setConsole(console);
        return Optional.of(factory.createCommand(tokens));
    }

    public void readExecuteCommand() {
        String line = "";
        try {
           line = console.nextLine();
        } catch (ConsoleEmptyException e) {
            console.printlnErr("Reached EOF");
        }

        if (line.isEmpty()) {
            return;
        }

        String[] tokens = line.split(" ");

        assert tokens.length > 0;

        Optional<Command> commandOptional;
        try {
            commandOptional = createCommandFromTokens(tokens);
        } catch (SubtypeScanError e) {
            return;
        }
        if (commandOptional.isEmpty()) {
            console.printlnErr("No such command `%s`".formatted(tokens[0]));
            return;
        }
        Command command = commandOptional.get();

        ExecutionResult response = server.response(command);

        handleResponse(response);
    }

    public void handleResponse(ExecutionResult response) {
        if (!(handlers.containsKey(response.getCommandName()))) {
            console.printlnErr("Cannot handle received result for command `%s`".formatted(response.getCommandName()));
            return;
        }
        ExecutionResultHandler handler = handlers.get(response.getCommandName());
        handler.setConsole(console);
        handler.handleResult(response);
    }

    public void repl() {
        while (console.hasNext()) {
            readExecuteCommand();
        }
        console.printlnWarn("EOF reached. exiting REPL...");
    }

    public static void main(String[] args) {
        ClientApplication app = new ClientApplication();
        ServerApplication server = new ServerApplication(new CollectionManager());

        app.connect(server);

        app.repl();
    }
}
