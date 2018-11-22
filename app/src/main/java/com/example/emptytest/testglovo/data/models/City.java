package com.example.emptytest.testglovo.data.models;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


//Model of City
public class City {

    @SerializedName("working_area")
    @Expose
    private List<String> workingArea = null;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("country_code")
    @Expose
    private String countryCode;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("enabled")
    @Expose
    private Boolean enabled;

    @SerializedName("busy")
    @Expose
    private Boolean busy;

    @SerializedName("time_zone")
    @Expose
    private String timeZone;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @SerializedName("language_code")
    @Expose
    private String languageCode;

    public List<String> getWorkingArea() {
        return workingArea;
    }

    public void setWorkingArea(List<String> workingArea) {
        this.workingArea = workingArea;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }



    public LatLng mPosition;
    public List<LatLng> mArea;
}