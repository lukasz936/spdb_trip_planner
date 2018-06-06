package com.tripplanner.controller.googleApi;

import android.widget.Toast;

import com.tripplanner.model.DataManager;
import com.tripplanner.model.Place;
import com.tripplanner.view.MapsActivity;

/**
 * Created by Łukasz on 27.05.2018.
 */

public class GoogleApiController {

    final private MapsActivity view;

    public GoogleApiController(MapsActivity view) {
        this.view = view;
    }

    public void sendRequest() {
        if (DataManager.getPlaces().size() < 2) {
            Toast.makeText(view, "Nie wprowadzono wystarczającej liczby miejsc", Toast.LENGTH_LONG).show();
            return;
        }
        String url = createUrl();
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask();
        requestAsyncTask.execute(url, view);

    }


    private String createUrl() {
        Place destinationPlace = DataManager.getPlaceById(DataManager.getPlaces().size() - 1);
            String origin = "origin=" + DataManager.userLocation.latitude + "," + DataManager.userLocation.longitude;
        String destination = "destination=" + destinationPlace.getLatLng().latitude + "," + destinationPlace.getLatLng().longitude;
        String sensor = "sensor=false";
        String mode = "mode=walking";
        String waypoints = "waypoints=optimize:true";
        for (int i = 0; i < DataManager.getPlaces().size() - 1; i++) {
            waypoints += "|via:" + DataManager.getPlaces().get(i).getLatLng().latitude + "," + DataManager.getPlaces().get(i).getLatLng().longitude;
        }
        String parameters = origin + "&" + destination + "&" + waypoints + "&" + sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

}
