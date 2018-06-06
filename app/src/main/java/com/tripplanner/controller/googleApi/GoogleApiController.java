package com.tripplanner.controller.googleApi;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.Place;
import com.tripplanner.model.RouteParam;
import com.tripplanner.model.RouteRequestData;
import com.tripplanner.model.TravelMode;
import com.tripplanner.view.MapsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz on 27.05.2018.
 */

public class GoogleApiController {

    final private MapsActivity view;

    public GoogleApiController(MapsActivity view) {
        this.view = view;
    }

    public void findRoute() {
        if (DataManager.getPlaces().size() < 2) {
            Toast.makeText(view, "Nie wprowadzono wystarczającej liczby miejsc", Toast.LENGTH_LONG).show();
            return;
        }
        DataManager.setRouteParam(new RouteParam());
        DataManager.setRouteRequestData(new RouteRequestData());
        for (int i = 0; i < DataManager.getPlaces().size(); ++i) {
            String url = createRouteUrl(DataManager.userLocation, DataManager.getPlaces().get(i).getLatLng(), createViasExceptPlace(i));
            RequestAsyncTask requestAsyncTask = new RequestAsyncTask();
            requestAsyncTask.execute(url, RouteRequestData.FIND_ROUTE);
        }
    }

    private List<LatLng> createViasExceptPlace(int placeIdx) {
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < DataManager.getPlaces().size(); ++i) {
            if (i == placeIdx) {
                latLngs.add(DataManager.getPlaces().get(i).getLatLng());
            }
        }
        return latLngs;
    }

    private String createRouteUrl(LatLng origin, LatLng destination, List<LatLng> vias) {
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=" + DataManager.getRouteParam().getTravelMode().name().toLowerCase();
        String waypoints = "waypoints=optimize:true";
        for (LatLng latLng : vias) {
            waypoints += "|" + latLng.latitude + "," + latLng.longitude;
        }
        String parameters = originStr + "&" + destinationStr + "&" + waypoints + "&" + sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private String createNearbyRestaurantNameUrl() {

        //String parameters = origin + "&" + destination + "&" + waypoints + "&" + sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/";
    }

    private String createNearbyRestaurantUrl() {

        //String parameters = origin + "&" + destination + "&" + waypoints + "&" + sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/";
    }

}
