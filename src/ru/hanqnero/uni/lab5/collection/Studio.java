package ru.hanqnero.uni.lab5.collection;

public class Studio {
    protected String name; //Поле может быть null
    protected String address; //Поле может быть null
    public Studio() {}

    public Studio setAddress(String address) {
        this.address = address;
        return this;
    }
    public Studio setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "'" + name + "@" + address + "'";
    }
}
