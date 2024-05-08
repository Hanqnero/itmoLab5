package ru.hanqnero.uni.lab5.client.invokers;

public abstract class AbstractCommandInvoker {
    private final String commandName;

    protected AbstractCommandInvoker(String commandName) {
        this.commandName = commandName;
    }

    public void invoke() {

    }
}
