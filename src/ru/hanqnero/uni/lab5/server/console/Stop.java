package ru.hanqnero.uni.lab5.server.console;

import ru.hanqnero.uni.lab5.server.ServerApplication;

public abstract class Stop {
    public static void accept(ServerApplication server) {
        server.stop();
    }
}
