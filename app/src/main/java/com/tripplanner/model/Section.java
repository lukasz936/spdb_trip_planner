package com.tripplanner.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Section {

    private int distance;
    private int duration;
    private LatLng startLocation;
    private LatLng endLocation;
    private List<List<LatLng>> polylines;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public List<List<LatLng>> getPolylines() {
        return polylines;
    }

    public void setPolylines(List<List<LatLng>> polylines) {
        this.polylines = polylines;
    }
}
