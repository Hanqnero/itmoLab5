package ru.hanqnero.uni.lab5.commons.util;

import ru.hanqnero.uni.lab5.client.factories.concrete.RemoveGreaterFactory;
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
            InfoResultHandler::new),
    SHOW(
            "show",
            "- Display list of every collection item",
            ShowFactory::new,
            ShowExecutor::new,
            ShowResultHandler::new),
    ADD(
            "add",
            "--[min|max] {Music Band} - Add element to collection",
            AddFactory::new,
            AddExecutor::new,
            AddResultHandler::new
    ),
    UPDATE(
            "update",
            "<id> {Music Band} - Update element in collection",
            UpdateFactory::new,
            UpdateExecutor::new,
            UpdateResultHandler::new),
    REMOVE(
            "remove",
            "--[id <id>|--studio {Studio}] - Remove element with matching id from collection",
            RemoveFactory::new,
            RemoveExecutor::new,
            RemoveResultHandler::new),
    CLEAR(
            "clear",
            "- Remove all items from the collection",
            ClearFactory::new,
            ClearExecutor::new,
            ClearResultHandler::new),

    SCRIPT(
            "execute",
            "<filename> - Execute list of commands from file",
            ScriptFactory::new,
            ScriptExecutor::new,
            ScriptResultHandler::new
    ),
    SAVE(
            "save",
            "- Save collection to text file",
            SaveFactory::new,
            SaveExecutor::new,
            SaveResultHandler::new
    ),
    REMOVE_GREATER(
            "remgr",
            "{Music Band} - Remove all elements exceeding this from collection",
            RemoveGreaterFactory::new,
            RemoveGreaterExecutor::new,
            RemoveGreaterResultHandler::new
    ),
    GET_BY(
            "get",
            "--[min|max] --[creation|establishment]- Display first or last element after chosen sorting",
            GetFactory::new,
            GetByExecutor::new,
            GetResultHandler::new
    ),
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
}
