package ru.hanqnero.uni.lab5.collection;

public class Studio {
    private String name; //Поле может быть null
    private String address; //Поле может быть null
    public Studio() {}

    public void setAddress(String address) {
        this.address = address;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
}
