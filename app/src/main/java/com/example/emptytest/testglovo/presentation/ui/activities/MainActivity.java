package com.example.emptytest.testglovo.presentation.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emptytest.testglovo.R;
import com.example.emptytest.testglovo.data.models.City;
import com.example.emptytest.testglovo.data.models.GlovoData;
import com.example.emptytest.testglovo.domain.ActivityActions;
import com.example.emptytest.testglovo.presentation.presenters.MainPresenter;
import com.example.emptytest.testglovo.presentation.ui.dialogs.CitySelector;
import com.example.emptytest.testglovo.presentation.utils.LocationTracker;
import com.example.emptytest.testglovo.presentation.utils.MapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//
//The main screen of the application. Implements a map — shows cities markers or
// work areas depending on scale.
//In the absence of permission to determine the location or if the location is not included
// in any of the work areas, the user is shown a window with a choice of cities.
public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityActions,
        LocationTracker.OnTrackerListener,
        CitySelector.OnCitySelectorInteractionListener  {

    private GoogleMap mMap;
    private ImageView ivFlag;
    private TextView tvAddress;

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivFlag = findViewById(R.id.ivFlag);
        tvAddress = findViewById(R.id.tvAddress);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment==null){
            //Handle ERROR
            return;
        }
        mapFragment.getMapAsync(this);

        mainPresenter = new MainPresenter();
        mainPresenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        mainPresenter.detach();
        super.onDestroy();
    }

    //Show dialog with Cities
    public void showCitySelector(){
        CitySelector selector = CitySelector.newInstance();
        selector.setListener(this);
        selector.show(getSupportFragmentManager(),CitySelector.TAG);
    }

    public void setAddress(String address){
        tvAddress.setText(address);
    }

    @Override
    public void onCitySelected(City city) {
        mainPresenter.userSelectedCity(city);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(Build.VERSION.SDK_INT<23 || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
            LocationTracker.getInstance(this);
            LocationTracker.getInstance().setOnTrackerListener(this);
            LocationTracker.getInstance().connect();
        }
        else {
            mainPresenter.handlePermissionLocation(null);
        }

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mainPresenter.mapChanged();
                findReadableAddress(mMap.getProjection().getVisibleRegion().latLngBounds.getCenter());
            }
        });

        mainPresenter.getData(this);
    }

    private void updateMarkers(GlovoData data){

        if(data==null) return;

        mMap.clear();

        for(City city:data.cities){
            if(city.mPosition!=null){

                if(mMap.getCameraPosition().zoom<=10 && MapUtils.CheckVisibility(mMap, city.mPosition)){
                        mMap.addMarker(new MarkerOptions().position(city.mPosition).title(city.getName()));
                }
                else if(city.mArea!=null && MapUtils.checkPolygon(mMap, city.mArea)){
                        Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                                .clickable(true)
                                .add(city.mArea.get(0),
                                        city.mArea.get(1),
                                        city.mArea.get(2),
                                        city.mArea.get(3))
                                .strokeColor(Color.RED)
                                .fillColor(0x330000FF));

                        LatLngBounds latLongBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                        LatLng center = latLongBounds.getCenter();

                        if(MapUtils.isPointInPolygon(center, polygon1.getPoints(),polygon1.isGeodesic())){
                            ivFlag.setImageDrawable(getResources().getDrawable(R.drawable.green));

                            return;
                        }

                        ivFlag.setImageDrawable(getResources().getDrawable(R.drawable.red));
                }
            }
        }
    }

    private void findReadableAddress(final LatLng point)
    {
        new Thread(() -> {
            Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
            try
            {
                final List<Address> addresses=geocoder.getFromLocation(point.latitude, point.longitude,1);
                if(addresses.size()>0)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAddress(addresses.get(0).getCountryName()+", "+ addresses.get(0).getAdminArea()+", "+ addresses.get(0).getLocality()+", "+ addresses.get(0).getAddressLine(0));
                        }
                    });
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }).start();
    }

/////////////////////////////////////////////////////

    @Override
    public void onUpdateMarkers(GlovoData data) {
        updateMarkers(data);
    }

    //Callback from LocationTracker
    @Override
    public void onTrackerConnected() {
        LocationTracker.getInstance().startLocationUpdate();
    }
    //Callback from LocationTracker
    @Override
    public void onTrackerSuspended() {

    }
    //Callback from LocationTracker
    @Override
    public void onTrackerFailed() {

    }
    //Callback from LocationTracker
    @Override
    public void onTrackerLocationChanged(Location location) {
        LocationTracker.getInstance().setOnTrackerListener(null);
        LocationTracker.getInstance().disconnect();
        mainPresenter.handlePermissionLocation(location);
    }

    @Override
    public void showPosition(LatLng position){
            MapUtils.positioningCamera(mMap,position,MapUtils.ZOOM,true);
    }


////////////////////////////////////////////////////
}
