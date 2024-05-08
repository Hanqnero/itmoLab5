package ru.hanqnero.uni.lab5.client;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleManager {
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

    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream out = System.out;

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public String nextLine() {
        return scanner.nextLine().trim();
    }

    public void println(String str) {
        out.println(str);
    }

    private void println(String str, ANSIColor color) {
        out.println(color + str + ANSIColor.RESET_COLOR);
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

    public static void main(String[] args) {
        var c = new ConsoleManager();
        c.printlnSuc("Hello World!");
    }
}
