package com.example.emptytest.testglovo.presentation.presenters;

import android.location.Location;

import com.example.emptytest.testglovo.data.models.City;
import com.example.emptytest.testglovo.data.models.GlovoData;
import com.example.emptytest.testglovo.domain.ActivityActions;
import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class MainPresenterTest {

    private MainPresenter presenter;
    private ActivityActions mockActivityActions;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockActivityActions=Mockito.mock(ActivityActions.class);

        presenter = new MainPresenter();
        presenter.attach(mockActivityActions);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detach();
    }

    @Test
    public void mapChangedAttached() {
        presenter.mapChanged();
        verify(mockActivityActions).onUpdateMarkers(any());
    }
    @Test
    public void mapChangedDetached() {
        presenter.detach();
        presenter.mapChanged();
        verify(mockActivityActions, never()).onUpdateMarkers(any());
    }

    @Test
    public void userSelectedCity() {
    }

    //If there is no permission to determine the location, need to show the city selection dialog
    @Test
    public void handlePermissionWithoutLocation() {
        presenter.handlePermissionLocation(null);
        verify(mockActivityActions).showCitySelector();
        verify(mockActivityActions,never()).showPosition(any());
    }

    //If location is allowed and location in the workspace, you need to display a map with the position
    @Test
    public void handlePermissionWithCorrectLocation() {
        Location mockLocation = Mockito.mock(Location.class);//new Location("CAI");
        when(mockLocation.getLatitude()).thenReturn(30.0444196);
        when(mockLocation.getLongitude()).thenReturn(31.2357116);
        List<City> listCities = new ArrayList<>();
        City city = new City();
        city.mArea = new ArrayList<>();
        city.mArea.add(new LatLng(29.9944196,31.185711599999998));
        city.mArea.add(new LatLng(30.094419600000002,31.185711599999998));
        city.mArea.add(new LatLng(30.094419600000002,31.2857116));
        city.mArea.add(new LatLng(29.9944196,31.2857116));
        listCities.add(city);

        presenter.mData = new GlovoData(listCities,null);
        presenter.handlePermissionLocation(mockLocation);
        verify(mockActivityActions,never()).showCitySelector();
        verify(mockActivityActions).showPosition(any());
    }

    //If location is allowed and the location is not in the workspace, need to show the city selection dialog
    @Test
    public void handlePermissionWithWrongLocation() {
        Location mockLocation = Mockito.mock(Location.class);//new Location("Odessa");
        when(mockLocation.getLatitude()).thenReturn(0.0);
        when(mockLocation.getLongitude()).thenReturn(0.0);
        List<City> listCities = new ArrayList<>();
        City city = new City();
        city.mArea = new ArrayList<>();
        city.mArea.add(new LatLng(29.9944196,31.185711599999998));
        city.mArea.add(new LatLng(30.094419600000002,31.185711599999998));
        city.mArea.add(new LatLng(30.094419600000002,31.2857116));
        city.mArea.add(new LatLng(29.9944196,31.2857116));
        listCities.add(city);

        presenter.mData = new GlovoData(listCities,null);
        presenter.handlePermissionLocation(mockLocation);
        verify(mockActivityActions).showCitySelector();
        verify(mockActivityActions,never()).showPosition(any());
    }


    // If there is no data about cities and their working areas, need to display the city selection dialog
    @Test
    public void handlePermissionWithLocationNoData() {
        presenter.handlePermissionLocation(new Location(""));
        verify(mockActivityActions).showCitySelector();
        verify(mockActivityActions,never()).showPosition(any());
    }

    @Test
    public void getData() {
    }
}