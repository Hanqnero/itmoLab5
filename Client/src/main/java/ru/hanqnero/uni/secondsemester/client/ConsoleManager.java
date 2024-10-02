package ru.hanqnero.uni.secondsemester.client;

import ru.hanqnero.uni.secondsemester.commons.exceptions.ConsoleEmptyException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleManager {
    private final OutputStream stdout;
    private final Scanner scanner;
    private final PrintStream printout;

    private boolean recursiveSubtypeScan = true;
    private final ScriptManager scriptManager = new ScriptManager();

    public ConsoleManager(InputStream stdin, OutputStream stdout) {
        this.stdout = stdout;
        scanner = new Scanner(stdin);
        this.printout = new PrintStream(stdout);
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public OutputStream getStdout() {
        return stdout;
    }

    public boolean isInteractive() {
        return recursiveSubtypeScan;
    }
    public void setInteractiveMode(boolean mode) {
        recursiveSubtypeScan = mode;
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

    @SuppressWarnings("unused")
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

    public void printlnWarn(String str) {
        println("W: " + str, ANSIColor.YELLOW);
    }

    @SuppressWarnings("unused")
    public void print(String s) {
        printout.print(s);
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
