package ru.hanqnero.uni.lab5.server.console;

import java.io.*;

public class ServerConsole {

    public static class ServerConsoleEOFException extends Exception {
        ServerConsoleEOFException() {super();}
    }

    private final BufferedReader reader;
    private final PrintStream writer;

    public ServerConsole(InputStream stdin, OutputStream stdout) {
        reader = new BufferedReader(new InputStreamReader(stdin));
        writer = new PrintStream(stdout);
    }

    public boolean ready() throws ServerConsoleEOFException {
        try {
            return reader.ready();
        } catch (IOException e) {
            throw new ServerConsoleEOFException();
        }
    }

    public String readLine() throws ServerConsoleEOFException {
        try {
             String line = reader.readLine();
             if (line == null) {
                 throw new ServerConsoleEOFException();
             }
             return line;
        } catch (IOException e) {
            throw new ServerConsoleEOFException();
        }
    }

    public void println(String s) {
        writer.println(s);
    }
}
