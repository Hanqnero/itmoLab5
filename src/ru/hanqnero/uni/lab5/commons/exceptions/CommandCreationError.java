package ru.hanqnero.uni.lab5.commons.exceptions;

public class CommandCreationError extends Exception {
    public CommandCreationError() {
        super();
    }
    public CommandCreationError(String message) {
        super(message);
    }
}
