package com.tripplanner.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Łukasz on 20.05.2018.
 */

public class Place {
    int id;
    LatLng latLng;
    String name;
    Date startDate;
    int duration;

    public Place(){}

    public Place(LatLng latLng) {
        this.latLng = latLng;
        this.name = String.format("%.2f", latLng.latitude) + "   " + String.format("%.2f", latLng.longitude);
    }

    public Place(LatLng latLng, String name) {
        this.latLng = latLng;
        this.name = name;
    }

    public Place(LatLng latLng, String name, int duration) {
        this.latLng = latLng;
        this.name = name;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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