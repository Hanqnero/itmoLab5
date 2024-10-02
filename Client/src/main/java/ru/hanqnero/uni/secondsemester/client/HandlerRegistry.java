package ru.hanqnero.uni.secondsemester.client;

import ru.hanqnero.uni.secondsemester.client.handlers.ExecutionResultHandler;
import ru.hanqnero.uni.secondsemester.commons.contract.results.ExecutionResult;

import java.util.HashMap;
import java.util.Optional;

public class HandlerRegistry {

    public record HandlerRegistryEntry(
            ExecutionResultHandler handler,
            String commandName
    ) {}

    private final HashMap<Class<? extends ExecutionResult>, HandlerRegistryEntry> registry = new HashMap<>();

    public void register(Class<? extends ExecutionResult> clazz, ExecutionResultHandler handler, String commandName) {
        var entry = new HandlerRegistryEntry(handler, commandName);
        registry.put(clazz, entry);
    }

    public Optional<HandlerRegistryEntry> find(Class<? extends ExecutionResult> clazz) {
        return Optional.ofNullable(registry.get(clazz));
    }
}
