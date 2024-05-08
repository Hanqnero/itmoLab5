package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.dto.CommandInfo;
import ru.hanqnero.uni.lab5.dto.commands.Command;
import ru.hanqnero.uni.lab5.dto.factories.CommandFactory;
import ru.hanqnero.uni.lab5.server.CollectionManager;
import ru.hanqnero.uni.lab5.server.ServerApplication;

import java.util.Map;
import java.util.Optional;

public class ClientApplication {
    private final ConsoleManager console = new ConsoleManager();
    private ServerApplication server;
    private final Map<String, CommandFactory> factories;

    public ClientApplication() {
        this.factories = CommandInfo.createAllFactoriesView();
    }

    public void connect(ServerApplication server) {
        this.server = server;
    }

    private Optional<Command> createCommandFromTokens(String[] tokens) {

        String commandName = tokens[0];

        var factory = this.factories.get(commandName);
        if (factory == null) {
            return Optional.empty();
        }

        factory.setConsoleManager(console);
        return Optional.of(factory.createCommand(tokens));
    }

    public void repl() {
        while (console.hasNext()) {
            String line = console.nextLine();

            if (line.isEmpty()) {
                console.printlnWarn("Invalid input: Empty line.\n");
                continue;
            }

            String[] tokens = line.split(" ");

            assert tokens.length > 0;

            Optional<Command> commandOptional = createCommandFromTokens(tokens);
            if (commandOptional.isEmpty()) {
                console.printlnErr("No such command `%s`".formatted(tokens[0]));
                continue;
            }
            Command command = commandOptional.get();

        }
    }

    public static void main(String[] args) {
        ClientApplication app = new ClientApplication();
        ServerApplication server = new ServerApplication(new CollectionManager());

        app.connect(server);

        app.repl();
    }
}
