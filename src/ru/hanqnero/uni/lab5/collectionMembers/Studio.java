package ru.hanqnero.uni.lab5.collectionMembers;

public class Studio {
    private String name; //Поле может быть null
    private String address; //Поле может быть null

    public Studio(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
