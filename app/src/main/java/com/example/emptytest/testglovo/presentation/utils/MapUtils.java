package com.example.emptytest.testglovo.presentation.utils;

import android.graphics.PointF;

import com.example.emptytest.testglovo.data.models.City;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class MapUtils {


    public static final int ZOOM=11;


    public static boolean checkPolygon(GoogleMap map, List<LatLng> polygon){
        for(LatLng latLng:polygon){
            if(CheckVisibility(map, latLng))
                return true;
        }
        return false;
    }

    public static boolean CheckVisibility(GoogleMap map, LatLng marker)
    {
        if(map != null)
        {
            LatLngBounds latLongBounds = map.getProjection().getVisibleRegion().latLngBounds;
            return latLongBounds.contains(marker);
        }
        return false;
    }

    public static boolean isPointInPolygon(LatLng point, List<LatLng> polygon, boolean geodesic){
        return PolyUtil.containsLocation(point, polygon,geodesic);
    }

    public static void positioningCamera(GoogleMap map, LatLng ll, float zoom, boolean fAnimate)
    {
        CameraPosition cp=new CameraPosition.Builder().target(ll).zoom(zoom).build();

        if(fAnimate)
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        else
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
    }
    public static boolean positioningCameraArea(GoogleMap map, List<LatLng> polygons, int iPadding, boolean fAnimate)
    {
        switch(polygons.size())
        {
            case 1:
                positioningCamera(map, polygons.get(0), ZOOM, fAnimate);
            case 0:
                return true;
        }
        {
            PointF ptSW=new PointF(1000.0f,1000.0f);
            PointF ptNE=new PointF(-1000.0f,-1000.0f);

            for(int i = 0; i< polygons.size(); i++)
            {
                if(polygons.get(i).latitude<ptSW.y)ptSW.y=(float) polygons.get(i).latitude;
                if(polygons.get(i).longitude<ptSW.x)ptSW.x=(float) polygons.get(i).longitude;
                if(polygons.get(i).latitude>ptNE.y)ptNE.y=(float) polygons.get(i).latitude;
                if(polygons.get(i).longitude>ptNE.x)ptNE.x=(float) polygons.get(i).longitude;
            }
            if(fAnimate)
                map.animateCamera
                        (
                                CameraUpdateFactory.newLatLngBounds
                                        (
                                                new LatLngBounds(new LatLng(ptSW.y,ptSW.x),new LatLng(ptNE.y,ptNE.x)),
                                                iPadding
                                        )
                        );
            else
                map.moveCamera
                        (
                                CameraUpdateFactory.newLatLngBounds
                                        (
                                                new LatLngBounds(new LatLng(ptSW.y,ptSW.x),new LatLng(ptNE.y,ptNE.x)),
                                                iPadding
                                        )
                        );
            return true;
        }
    }
    public static boolean checkPositionInWorkAreas(LatLng position, List<City> cities){
        for (City city: cities){
            if(city.mArea!=null && !city.mArea.isEmpty() && MapUtils.isPointInPolygon(position,city.mArea,false)){
                return true;
            }
        }
        return false;
    }
}
