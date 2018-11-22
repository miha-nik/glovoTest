package com.example.emptytest.testglovo.data.models;

import java.util.List;

//United Model of Cities and Countries
public class GlovoData {
    public List<City> cities;
    public List<Country> countries;

    public GlovoData(List<City> cities, List<Country> countries){
        this.cities = cities;
        this.countries = countries;
    }
}
