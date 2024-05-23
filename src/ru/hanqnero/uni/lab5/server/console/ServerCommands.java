package ru.hanqnero.uni.lab5.server.console;

import ru.hanqnero.uni.lab5.server.ServerApplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public enum ServerCommands {
    SAVE("save", Save::accept),
    STOP("stop", Stop::accept),
    ;
    private final String name;
    private final Consumer<ServerApplication> command;
    ServerCommands(String name, Consumer<ServerApplication> command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }
    public Consumer<ServerApplication> getCommand() {
        return command;
    }

    public static Map<String, Consumer<ServerApplication>> getAllCommands() {
        Map<String, Consumer<ServerApplication>> commands = new HashMap<>();
        Arrays.stream(values()).forEach(c -> commands.put(c.getName(), c.getCommand()));
        return commands;
    }
}
