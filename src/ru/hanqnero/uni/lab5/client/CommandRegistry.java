package ru.hanqnero.uni.lab5.client;


import ru.hanqnero.uni.lab5.client.factories.CommandFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class CommandRegistry {

    private final HashMap<String, RegistryEntry> registry = new HashMap<>();

    public record RegistryEntry(
                String commandName,
                CommandFactory commandFactory,
                String commandDescription
    ) {
        public String createHelpString() {
            return commandName+commandDescription;
        }
    }

    public void register(
            String commandName,
            CommandFactory commandFactory,
            String commandDescription) {

        var entry = new RegistryEntry(
                commandName,
                commandFactory,
                commandDescription
        );
        registry.put(commandName, entry);
    }

    public Optional<RegistryEntry> findEntry(String commandName) {
        return Optional.ofNullable(registry.get(commandName));
    }

    public Collection<RegistryEntry> getEntries() {
        return registry.values();
    }

    }
