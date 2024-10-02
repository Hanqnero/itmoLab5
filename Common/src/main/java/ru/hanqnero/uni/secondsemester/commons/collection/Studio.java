package ru.hanqnero.uni.secondsemester.commons.collection;

import java.io.Serializable;
import java.util.Objects;

public class Studio implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Studio studio = (Studio) o;
        return Objects.equals(name, studio.name) && Objects.equals(address, studio.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @SuppressWarnings("unused")
    public boolean equals(Studio o) {
        if (this == o) return true;
        if (o == null) return false;
        return this.address.equalsIgnoreCase(o.address) && this.name.equalsIgnoreCase(o.name);
    }
}
