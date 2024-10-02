package ru.hanqnero.uni.secondsemester.server.console;

import ru.hanqnero.uni.secondsemester.server.ServerApplication;

public abstract class Save {
    public static void accept(ServerApplication server) {
        server.getCollection().saveToFile();
    }
}
