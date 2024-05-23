package ru.hanqnero.uni.lab5.contract.commands;

import java.io.Serializable;

public interface Command extends Serializable{
    String getName();
}