package com.tripplanner.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private double distance;
    private int duration;
    public List<LatLng> points = new ArrayList<>();
}
