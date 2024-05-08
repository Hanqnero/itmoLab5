package ru.hanqnero.uni.lab5.server.receiver;

import ru.hanqnero.uni.lab5.dto.commands.Command;

public abstract class AbstractCommandReceiver {
    private String commandName;

    protected AbstractCommandReceiver(String commandName) {
        this.commandName = commandName;
    }

    public void receive(Command command) {

    }

}
