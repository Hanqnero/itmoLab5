package ru.hanqnero.uni.lab5.util.exceptions;

public class SubtypeScanError extends Exception {
    public SubtypeScanError(String string, String subtypeName) {
        super("Error while scanning subtype `%s`. Got invalid input `%s` while on non-interactive mode"
                .formatted(string, subtypeName));
    }
}
