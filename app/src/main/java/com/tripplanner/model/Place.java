package com.tripplanner.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Łukasz on 20.05.2018.
 */

public class Place {
    LatLng latLng;;
    String name;
    Date startDate;
    int duration;

    public Place(LatLng latLng) {
        this.latLng = latLng;
    }

    public Place(LatLng latLng, String name) {
        this.latLng = latLng;
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
