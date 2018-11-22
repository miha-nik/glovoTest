package com.example.emptytest.testglovo.data.models;

import java.util.List;

//Model of `City selector list
public class Item {
    private String name;
    private City city = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
