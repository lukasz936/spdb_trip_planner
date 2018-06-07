package com.tripplanner.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteRequestData {

    public static int FIND_ROUTE = 1;
    public static int FIND_SECTION = 2;
    public static int FIND_RESTAURANT = 3;
    public int requestCounter = 0;

    public List<Route> routes = new ArrayList<>();
    public List<LatLng> restaurantsList = new ArrayList<>();

}
