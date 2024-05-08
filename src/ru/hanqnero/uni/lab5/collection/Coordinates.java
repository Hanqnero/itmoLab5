package ru.hanqnero.uni.lab5.collection;

public class Coordinates {
    private long x;
    private Integer y; //Поле не может быть null

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
