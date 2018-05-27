package com.tripplanner.controller.googleApi;

import android.provider.ContactsContract;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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

    public void sendRequest(){
        if(DataManager.getPlaces().size()<2){
            Toast.makeText(view, "Nie wprowadzono wystarczającą liczbę miejsc", Toast.LENGTH_LONG).show();
            return;
        }
        String url = createUrl();
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask();
        requestAsyncTask.execute(url);

    }


    private String createUrl() {
        Place originPlace = DataManager.getPlace(0);
        Place destinationPlace = DataManager.getPlace(DataManager.getPlaces().size()-1);
        String origin = "origin=" + originPlace.getLatLng().latitude + "," + originPlace.getLatLng().longitude;
        String destination = "destination=" + destinationPlace.getLatLng().latitude + "," + destinationPlace.getLatLng().longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String parameters = origin + "&" + destination + "&" + sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

}
