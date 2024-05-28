package ru.hanqnero.uni.lab5.client;

import java.io.File;
import java.util.Stack;

public class ScriptManager {

    private final Stack<File> scriptExecutionStack = new Stack<>();

    public boolean checkScriptRecursion(File newScript) {
        return scriptExecutionStack.contains(newScript);
    }

    public void addScriptToStack(File newScript) {
        scriptExecutionStack.push(newScript);

        if (ClientApplication.DEBUG)
            System.out.println("Added script to head of stack: " + newScript);
    }

    public boolean removeScriptFromStack(File script) {
        if (ClientApplication.DEBUG) {
            System.out.println("Trying to remove script from head of stack: " + script);
            System.out.println("Current stack: " + scriptExecutionStack);
        }
        if (scriptExecutionStack.peek().equals(script)) {
            scriptExecutionStack.pop();
            return true;
        }
        return false;
    }
}
