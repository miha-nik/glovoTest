package com.example.emptytest.testglovo.data.repositories;

import com.example.emptytest.testglovo.data.repositories.db.DBManager;
import com.example.emptytest.testglovo.domain.DataReceiver;
import com.example.emptytest.testglovo.data.repositories.restful.RestFulAPI;
import com.example.emptytest.testglovo.data.models.City;
import com.example.emptytest.testglovo.data.models.Country;
import com.example.emptytest.testglovo.data.models.GlovoData;

import java.util.List;

import io.reactivex.annotations.NonNull;

//Implement a pattern repository
public class Repository {

    public void getDataFromNetwork(@NonNull DataReceiver callbacks){

        if(DBManager.data!=null
                && DBManager.data.countries!=null
                && !DBManager.data.countries.isEmpty()
                && DBManager.data.cities!=null
                && !DBManager.data.cities.isEmpty()) {
            callbacks.onSuccess(DBManager.data);
            return;
        }

        RestFulAPI api = new RestFulAPI();
        api.getData(new DataReceiver<GlovoData>() {
            @Override
            public void onSuccess(GlovoData data) {
                DBManager.data = data;
                callbacks.onSuccess(data);
            }

            @Override
            public void onError() {
                callbacks.onError();
            }
        });
    }

    public List<Country> getCountires(){
        return DBManager.data.countries;
    }

    public List<City> getCities(){
        return DBManager.data.cities;
    }
}