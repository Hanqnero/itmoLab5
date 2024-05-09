package ru.hanqnero.uni.lab5.contract;

import ru.hanqnero.uni.lab5.client.factories.concrete.*;
import ru.hanqnero.uni.lab5.client.handlers.concrete.*;
import ru.hanqnero.uni.lab5.server.executors.CommandExecutor;
import ru.hanqnero.uni.lab5.server.executors.concrete.*;
import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.client.handlers.ExecutionResultHandler;

import java.util.*;
import java.util.function.Supplier;

public enum CommandInfo {
    HELP(
            "help",
            "- Display list of commands",
            HelpFactory::new,
            HelpExecutor::new,
            HelpResultHandler::new
    ),
    EXIT(
            "exit",
            "- Stop client without saving any changes",
            ExitFactory::new,
            ExitExecutor::new,
            ExitResultHandler::new
    ),

    INFO(
            "info",
            "- Display information about collection",
            InfoFactory::new,
            InfoExecutor::new,
            InfoHandler::new),
    SHOW(
            "show",
            "- Display list of every collection item",
            ShowFactory::new,
            ShowExecutor::new,
            ShowHandler::new),
ADD(
        "add",
        "{Music Band} - Add element to collection",
        AddFactory::new,
        AddExecutor::new,
        AddResultHandler::new
),
//    UPDATE(
//            "update",
//            "<id> {Music Band} - Update element in collection",null,null),
//    REMOVE(
//            "remove",
//            "<id> - Remove element with matching id from collection",null,null),
//    CLEAR(
//            "clear",
//            "- Remove all items from the collection",null,null),
//    EXECUTE(
//            "execute",
//            "<filename> - Execute list of commands from file",null,null),
//    SAVE(
//            "save",
//            "- Save collection to text file",null,null),
//    ADD_IF_MAX(
//            "add_if_max",
//            "{Music Band} - Add new element to collection if it is greater than maximum element",null,null),
//    ADD_IF_MIN(
//            "add_if_min",
//            "{Music Band} - Add new element to collection if it is less than minimum element",null,null),
//    REMOVE_GR(
//            "remove_greater",
//            "{Music Band} - Remove all elements exceeding this from collection",null,null),
//    REMOVE_STUDIO(
//            "remove_by_studio",
//            "{Studio} - Remove all elements with matching studio",null,null),
//    MAX_BY_EST_DATE(
//            "max_by_establishment",
//            "- Display maximum element by establishment date",null,null),
    ;

    private final String name;
    private final String description;
    private final Supplier<CommandFactory> factorySupplier;
    private final Supplier<CommandExecutor> executorSupplier;
    private final Supplier<ExecutionResultHandler> handlerSupplier;

    CommandInfo(String name,
                String description,
                Supplier<CommandFactory> factory,
                Supplier<CommandExecutor> executor,
                Supplier<ExecutionResultHandler> handler
                ) {
        this.name = name;
        this.description = description;
        this.factorySupplier = factory;
        this.executorSupplier = executor;
        this.handlerSupplier = handler;
    }

    public static Map<String, ExecutionResultHandler> createHandlersView(CommandInfo... commands) {
        Map<String, ExecutionResultHandler> handlers = new HashMap<>();
        for (var c: commands) {
            handlers.put(c.name, c.handlerSupplier.get());
        }
        return handlers;
    }

    public static Map<String, ExecutionResultHandler> createHandlersView() {
        return createHandlersView(values());
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

    public static Map<String, CommandFactory> createFactoriesView(CommandInfo... commands) {
        Map<String, CommandFactory> factories = new HashMap<>();
        for (var c: commands) {
            factories.put(c.name, c.factorySupplier.get());
        }
        return factories;
    }

    public static Map<String, CommandFactory> createFactoriesView() {
        return createFactoriesView(CommandInfo.values());
    }

    public static Map<String, CommandExecutor> createExecutorsView(CommandInfo... commands) {
        Map<String, CommandExecutor> executors = new HashMap<>();
        for (var c: commands) {
            executors.put(c.name, c.executorSupplier.get());
        }
        return executors;
    }

    public static Map<String, CommandExecutor> createExecutorsView() {
        return createExecutorsView(values());
    }
}
