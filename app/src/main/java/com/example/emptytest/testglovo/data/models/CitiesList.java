package com.example.emptytest.testglovo.data.models;

import java.util.List;

//Model of Items for Cities selector(pass-through list)
public class CitiesList {
    private List<Item> listOfElements;
    private int count;

    public List<Item> getListOfElements() {
        return listOfElements;
    }

    public void setListOfElements(List<Item> listOfElements) {
        this.listOfElements = listOfElements;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
