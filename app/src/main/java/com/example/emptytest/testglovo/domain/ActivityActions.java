package com.example.emptytest.testglovo.domain;

import com.example.emptytest.testglovo.data.models.GlovoData;
import com.google.android.gms.maps.model.LatLng;

public interface ActivityActions {
    public void onUpdateMarkers(GlovoData data);
    public void showCitySelector();
    public void showPosition(LatLng position);
}
