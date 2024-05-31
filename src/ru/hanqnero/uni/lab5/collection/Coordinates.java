package ru.hanqnero.uni.lab5.collection;

import java.io.Serializable;

public class Coordinates implements Serializable {
    protected final long x;
    protected final Integer y; //Поле не может быть null

    public Coordinates(long x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
