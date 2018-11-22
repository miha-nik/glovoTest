package com.example.emptytest.testglovo.presentation.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.emptytest.testglovo.R;
import com.example.emptytest.testglovo.data.models.GlovoData;
import com.example.emptytest.testglovo.domain.DataReceiver;
import com.example.emptytest.testglovo.data.repositories.Repository;
import com.example.emptytest.testglovo.presentation.utils.Utils;


//
//StartActivity shows the download screen and asks the user permission to use his location
//Loads data from server
public class StartActivity extends AppCompatActivity{

    private static final int REQUEST_PERMISSION_LOCATION=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(!Utils.isInternetConnected(this))
            ErrorData();

        Repository repo = new Repository();
        repo.getDataFromNetwork(new DataReceiver<GlovoData>() {
            @Override
            public void onSuccess(GlovoData data) {
                if(data !=null && !data.cities.isEmpty() && !data.countries.isEmpty()){
                    //GOTO MAIN
                    checkLocationPermission();
                   return;
                }
                ErrorData();
            }


            @Override
            public void onError() {
                ErrorData();
            }
        });
    }

    private void ErrorData(){
        Toast.makeText(this, R.string.error_data_resived,Toast.LENGTH_LONG).show();
    }

    private void checkLocationPermission()
    {
        if(Build.VERSION.SDK_INT>=23&&checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions
                    (
                            this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSION_LOCATION
                    );
            return;
        }
        else {
            switchToMain();
        }
    }
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode)
        {
            case REQUEST_PERMISSION_LOCATION:
                if(grantResults.length<1||grantResults[0]==PackageManager.PERMISSION_DENIED)
                {
                    requestPermissionWithRationale();

                }
//                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
//                {
//
//                }
                switchToMain();
        }
    }

    public void requestPermissionWithRationale()
    {
        if(Build.VERSION.SDK_INT<23)return;
        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            Toast.makeText(this, R.string.location_permissin_description,Toast.LENGTH_LONG).show();
            return;
        }
        checkLocationPermission();
    }
    private void switchToMain(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}
