package ru.hanqnero.uni.lab5.collectionMembers;

public class Coordinates {
    private long x;
    private Integer y; //Поле не может быть null

    public Coordinates(long x, Integer y) {
        if (y == null) throw new IllegalArgumentException("Coordinate Y cannot be null");
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }
}
