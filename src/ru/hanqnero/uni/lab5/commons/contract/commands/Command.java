package ru.hanqnero.uni.lab5.commons.contract.commands;

import java.io.Serializable;

public interface Command extends Serializable{
    String getName();
}