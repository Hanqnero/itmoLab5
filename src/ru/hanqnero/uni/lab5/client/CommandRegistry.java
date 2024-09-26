package ru.hanqnero.uni.lab5.client;


import org.jetbrains.annotations.Nullable;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

import java.util.HashMap;

public class CommandRegistry {

    private final HashMap<Class<? extends Command>, RegistryEntry> registry = new HashMap<>();

    public record RegistryEntry(
                Class<? extends Command> commandClass,
                String commandName,
                CommandFactory commandFactory,
                ExecutionResultHandler resultHandler,
                String commandDescription
        ) {}

    public void register(
            Class<? extends Command> command,
            String commandName,
            CommandFactory commandFactory,
            ExecutionResultHandler resultHandler,
            String commandDescription) {

        var entry = new RegistryEntry(
                command,
                commandName,
                commandFactory,
                resultHandler,
                commandDescription
        );
        registry.put(command, entry);
    }

    public @Nullable RegistryEntry findEntry(Class<Command> command) {
        return registry.get(command);
    }

    public @Nullable RegistryEntry findEntry(String commandName) {
        return registry.values().stream()
                .filter(entry -> entry.commandName.equals(commandName))
                .findFirst()
                .orElse(null);
    }

    }
