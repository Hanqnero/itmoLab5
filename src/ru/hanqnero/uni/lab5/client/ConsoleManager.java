package ru.hanqnero.uni.lab5.client;

import ru.hanqnero.uni.lab5.util.exceptions.ConsoleEmptyException;

import java.io.*;
import java.util.Scanner;

public class ConsoleManager {
    private final InputStream defaultStdin;
    private final OutputStream defaultStdout;

    private InputStream stdin;
    private OutputStream stdout;
    private Scanner scanner;
    private PrintStream printout;

    private boolean recursiveSubtypeScan = true;


    public ConsoleManager(InputStream stdin, OutputStream stdout) {
        defaultStdin = stdin;
        defaultStdout = stdout;

        this.stdin = stdin;
        this.stdout = stdout;
        scanner = new Scanner(System.in);
        this.printout = new PrintStream(stdout);
    }

    public boolean isInteractive() {
        return recursiveSubtypeScan;
    }
    public void setInteractiveMode(boolean mode) {
        recursiveSubtypeScan = mode;
    }

    public void setStdin(InputStream stdin) {
        this.stdin = stdin;
        this.scanner = new Scanner(stdin);
    }

    public void setDefaultStdin() {
        this.stdin = defaultStdin;
        this.scanner = new Scanner(defaultStdin);
    }

    public void setStdout(OutputStream stdout) {
        this.stdout = stdout;
        this.printout = new PrintStream(stdout);
    }

    public void setDefaultStdout() {
        this.stdout = defaultStdout;
        this.printout = new PrintStream(stdout);
    }

    public boolean hasNext() {
        return scanner.hasNextLine();
    }

    public String nextLine() throws ConsoleEmptyException {
        if (!scanner.hasNextLine()) {
            throw new ConsoleEmptyException();
        }
        return scanner.nextLine();
    }

    public String nextLine(String inputHint) throws ConsoleEmptyException {
        printout.print(inputHint);
        return nextLine();
    }

    public void println(String str) {
        printout.println(str);
    }

    private void println(String str, ANSIColor color) {
        printout.println(color + str + ANSIColor.RESET_COLOR);
    }


    public void printlnErr(String str) {
        println("E: " + str, ANSIColor.RED);
    }

    public void printlnSuc(String str) {
        println("S: "+str, ANSIColor.GREEN);
    }

    public static String ensureString(Object o) {
        return o == null ? "NULL" : o.toString();
    }

    public void printlnWarn(String str) {
        println("W: " + str, ANSIColor.YELLOW);
    }

    private enum ANSIColor {
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        RESET_COLOR("\u001B[0m");

        private final String code;
        ANSIColor(String code) {
            this.code = code;
        }
        @Override
        public String toString() {
            return code;
        }
    }
}
