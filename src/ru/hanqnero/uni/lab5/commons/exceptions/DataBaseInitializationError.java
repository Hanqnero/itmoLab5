package ru.hanqnero.uni.lab5.commons.exceptions;

public class DataBaseInitializationError extends RuntimeException {
    public DataBaseInitializationError(String message) {
        super(message);
    }
}
