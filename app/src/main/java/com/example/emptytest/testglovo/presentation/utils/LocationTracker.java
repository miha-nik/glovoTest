package com.example.emptytest.testglovo.presentation.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationTracker implements
  GoogleApiClient.ConnectionCallbacks,
  GoogleApiClient.OnConnectionFailedListener,
        LocationListener
  {
    public interface OnTrackerListener
      {
        public void onTrackerConnected();
        public void onTrackerSuspended();
        public void onTrackerFailed();
        public void onTrackerLocationChanged(Location location);
      }

    private static LocationTracker instance=null;

    private OnTrackerListener onTrackerListener=null;

    private GoogleApiClient googleApiClient=null;
    private Location location=null;

    public static LocationTracker getInstance(Context context)
      {
        if(instance!=null)return instance;
        return instance=new LocationTracker(context);
      }
    public static LocationTracker getInstance()
      {
        return instance;
      }

    private LocationTracker(Context context)
      {
        // Create an instance of GoogleAPIClient.
        if(googleApiClient==null)
          {
            googleApiClient=new GoogleApiClient.Builder(context)
              .addApi(LocationServices.API)
              .addConnectionCallbacks(this)
              .addOnConnectionFailedListener(this)
              .build();
          }
      }

    public void setOnTrackerListener(OnTrackerListener onTrackerListener)
      {
        this.onTrackerListener=onTrackerListener;
      }
    public void connect()
      {
        googleApiClient.connect();
      }
    public void startLocationUpdate()
      {
        if(ActivityCompat.checkSelfPermission(googleApiClient.getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(googleApiClient.getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
          {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
          }
        {
          LocationRequest locationRequest=new LocationRequest();

          locationRequest.setInterval(5000);
          locationRequest.setFastestInterval(2000);
          locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
          LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }
      }
    public void stopLocationUpdate()
      {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
      }
    public void disconnect()
      {
        googleApiClient.disconnect();
      }
    public Location getLocation()
      {
//        if(ActivityCompat.checkSelfPermission(googleApiClient.getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(googleApiClient.getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
//          {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return null;
//          }
//        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        return location;
      }

    @Override public void onConnected(@Nullable Bundle bundle)
      {
        if(ActivityCompat.checkSelfPermission(googleApiClient.getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(googleApiClient.getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
          {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
          }
        location=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(onTrackerListener!=null)
          onTrackerListener.onTrackerConnected();
      }
    @Override public void onConnectionSuspended(int i)
      {
        if(onTrackerListener!=null)
          onTrackerListener.onTrackerSuspended();
      }
    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
      {
        if(onTrackerListener!=null)
          onTrackerListener.onTrackerFailed();
      }

    @Override public void onLocationChanged(Location location)
      {
        this.location=location;
        if(onTrackerListener!=null)
          onTrackerListener.onTrackerLocationChanged(location);
      }
  }
