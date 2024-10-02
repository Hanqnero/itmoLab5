package ru.hanqnero.uni.secondsemester.server.console;

import ru.hanqnero.uni.secondsemester.server.ServerApplication;

public abstract class Stop {
    public static void accept(ServerApplication server) {
        server.stop();
    }
}
