package com.example.emptytest.testglovo.data.repositories.restful;

import com.example.emptytest.testglovo.domain.DataReceiver;
import com.example.emptytest.testglovo.data.models.City;
import com.example.emptytest.testglovo.data.models.Country;
import com.example.emptytest.testglovo.data.models.GlovoData;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

//Makes web request to server and receives response.
public class RestFulAPI {

    List<City> mCities = null;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RestFulAPI(){

    }
    public void Destroy(){
        compositeDisposable.clear();
    }

    public void getData(@NonNull DataReceiver callbacks){
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService = ApiUtils.getAPIService();

        Observable<List<City>> citiesStream =  mAPIService.Cities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<List<Country>> countriesStream =   mAPIService.Countries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Disposable disposable = Observable.zip(citiesStream, countriesStream, new BiFunction<List<City>, List<Country>, GlovoData>() {
            @Override
            public GlovoData apply(List<City> cities, List<Country> countries) throws Exception {
                return new GlovoData(cities, countries);
            }
        }).subscribe(response -> {
            callbacks.onSuccess(response);
        },throwable->{
            callbacks.onError();
        });

        compositeDisposable.add(disposable);
    }
}
