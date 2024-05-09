package ru.hanqnero.uni.lab5.collection;

public class Studio {
    private String name; //Поле может быть null
    private String address; //Поле может быть null
    public Studio() {}

    public Studio setAddress(String address) {
        this.address = address;
        return this;
    }
    public Studio setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "'" + name + "@" + address + "'";
    }
}
