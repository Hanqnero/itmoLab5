package ru.hanqnero.uni.lab5.dto;

import ru.hanqnero.uni.lab5.dto.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.dto.factories.CommandFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public enum CommandInfo {
    HELP(
            "help",
            "- Display list of commands",null,null),

    INFO(
            "info",
            "- Display information about collection",null,null),
    SHOW(
            "show",
            "- Display list of every collection item",null,null),
    ADD(
            "add",
            "{Music Band} - Add element to collection",null,null),
    UPDATE(
            "update",
            "<id> {Music Band} - Update element in collection",null,null),
    REMOVE(
            "remove",
            "<id> - Remove element with matching id from collection",null,null),
    CLEAR(
            "clear",
            "- Remove all items from the collection",null,null),
    EXECUTE(
            "execute",
            "<filename> - Execute list of commands from file",null,null),
    EXIT(
            "exit",
            "- Stop server without saving any changes",null,null),
    SAVE(
            "save",
            "- Save collection to text file",null,null),
    ADD_IF_MAX(
            "add_if_max",
            "{Music Band} - Add new element to collection if it is greater than maximum element",null,null),
    ADD_IF_MIN(
            "add_if_min",
            "{Music Band} - Add new element to collection if it is less than minimum element",null,null),
    REMOVE_GR(
            "remove_greater",
            "{Music Band} - Remove all elements exceeding this from collection",null,null),
    REMOVE_STUDIO(
            "remove_by_studio",
            "{Studio} - Remove all elements with matching studio",null,null),
    MAX_BY_EST_DATE(
            "max_by_establishment",
            "- Display maximum element by establishment date",null,null),
    ;

    private final String name;
    private final String description;
    private final Supplier<CommandFactory> factorySupplier;
    private final Supplier<CommandExecutor> executorSupplier;

    CommandInfo(String name, String description, Supplier<CommandFactory> factory, Supplier<CommandExecutor> executor) {
        this.name = name;
        this.description = description;
        this.factorySupplier = factory;
        this.executorSupplier = executor;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CommandFactory getFactoryForName(String commandName) {
        var command = Arrays.stream(CommandInfo.values())
                .filter(c -> c.getName().equals(commandName))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        return command.factorySupplier.get();
    }

    public CommandExecutor getExecutorForName(String commandName) {
        var command = Arrays.stream(CommandInfo.values())
                .filter(c -> c.getName().equals(commandName))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        return command.executorSupplier.get();
    }

    public static Map<String, CommandFactory> createAllFactoriesView() {
        Map<String, CommandFactory> factories = new HashMap<>();
        for (var c: CommandInfo.values()) {
            factories.put(c.name, c.factorySupplier.get());
        }
        return factories;
    }

    public static Map<String, CommandExecutor> createAllExecutorsView() {
        Map<String, CommandExecutor> executors = new HashMap<>();
        for (var c: CommandInfo.values()) {
            executors.put(c.name, c.executorSupplier.get());
        }
        return executors;
    }
}
