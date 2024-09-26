package ru.hanqnero.uni.lab5.client;


import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;

import java.util.HashMap;
import java.util.Optional;

public class CommandRegistry {

    private final HashMap<String, RegistryEntry> registry = new HashMap<>();

    public record RegistryEntry(
                CommandFactory commandFactory,
                String commandDescription
        ) {}

    public void register(
            String commandName,
            CommandFactory commandFactory,
            String commandDescription) {

        var entry = new RegistryEntry(
                commandFactory,
                commandDescription
        );
        registry.put(commandName, entry);
    }

    public Optional<RegistryEntry> findEntry(String commandName) {
        return Optional.ofNullable(registry.get(commandName));
    }

    }
