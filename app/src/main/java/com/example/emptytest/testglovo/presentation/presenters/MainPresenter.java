package com.example.emptytest.testglovo.presentation.presenters;

import android.content.Context;
import android.location.Location;

import com.example.emptytest.testglovo.data.models.City;
import com.example.emptytest.testglovo.data.models.GlovoData;
import com.example.emptytest.testglovo.data.repositories.Repository;
import com.example.emptytest.testglovo.domain.ActivityActions;
import com.example.emptytest.testglovo.domain.DataReceiver;
import com.example.emptytest.testglovo.presentation.utils.MapUtils;
import com.example.emptytest.testglovo.presentation.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


//
//Presenter for class MainActivity
//
public class MainPresenter {

    public GlovoData mData = null;
    private ActivityActions mActions;

    public void attach(ActivityActions actions){
        mActions = actions;
    }
    public void detach(){
        mActions = null;
    }

    public void mapChanged(){
        if(mActions!=null)
            mActions.onUpdateMarkers(mData);
    }

    public void userSelectedCity(City city){
        if(mActions!=null)
            mActions.showPosition(city.mPosition);
    }


// Shows the user's location on the map or offers
// select from the list of cities if there is no permission to determin
// locations or if the location is not part of the workspace
    public void handlePermissionLocation(Location userPosition){
        if(userPosition==null){
            if(mActions!=null)
                mActions.showCitySelector();
            return;
        }
        if(mData!=null){
            LatLng position = new LatLng(userPosition.getLatitude(),userPosition.getLongitude());
            if(mActions!=null && MapUtils.checkPositionInWorkAreas(position, mData.cities)){
                mActions.showPosition(position);
                return;
            }
        }
        if(mActions!=null)
            mActions.showCitySelector();
    }

    //Requests data from the repository and modifies workspaces.
    public void getData(Context context){
        Repository r = new Repository();
        r.getDataFromNetwork(new DataReceiver<GlovoData>() {
            @Override
            public void onSuccess(GlovoData data) {
                mData = data;
                String citiesData = null;
                try {

                    //FACKE DATA. This Data mast be received from server
                    {
                        citiesData = new String(Utils.getData(context, "data.json"),"utf-8");
                        JSONArray d = new JSONArray(citiesData);
                        for(int i=0;i<d.length();i++){
                            for(City city: data.cities){
                                JSONObject object = d.getJSONObject(i);
                                if(city.getCode().equals(object.getString("code"))){
                                    city.mPosition = new LatLng(object.getJSONObject("latlng").getDouble("lat"), object.getJSONObject("latlng").getDouble("lng"));
                                    city.mArea = new ArrayList<>();

                                    city.mArea.add(new LatLng(object.getJSONArray("area").getJSONObject(0).getDouble("lat"), object.getJSONArray("area").getJSONObject(0).getDouble("lng")));
                                    city.mArea.add(new LatLng(object.getJSONArray("area").getJSONObject(1).getDouble("lat"), object.getJSONArray("area").getJSONObject(1).getDouble("lng")));
                                    city.mArea.add(new LatLng(object.getJSONArray("area").getJSONObject(2).getDouble("lat"), object.getJSONArray("area").getJSONObject(2).getDouble("lng")));
                                    city.mArea.add(new LatLng(object.getJSONArray("area").getJSONObject(3).getDouble("lat"), object.getJSONArray("area").getJSONObject(3).getDouble("lng")));
                                }
                            }
                        }
                    }

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(mActions!=null)
                    mActions.onUpdateMarkers(mData);
            }

            @Override
            public void onError() {

            }
        });
    }

}
