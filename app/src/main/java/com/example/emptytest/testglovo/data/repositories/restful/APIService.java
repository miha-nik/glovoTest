package com.example.emptytest.testglovo.data.repositories.restful;

import com.example.emptytest.testglovo.data.models.City;
import com.example.emptytest.testglovo.data.models.Country;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface APIService {
    @Headers("Content-Type: application/json")
    @GET("countries")
    Observable<List<Country>> Countries();

    @Headers("Content-Type: application/json")
    @GET("cities")
    Observable<List<City>> Cities();

    @Headers("Content-Type: application/json")
    @GET("/api-mobile")
    City CityBuId(@Body RequestBody body);
}