package com.tripplanner.controller.googleApi;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.Place;
import com.tripplanner.model.Route;
import com.tripplanner.model.RouteParam;
import com.tripplanner.model.RouteRequestData;
import com.tripplanner.model.Section;
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
        DataManager.setRouteRequestData(new RouteRequestData());
        for (int i = 0; i < DataManager.getPlaces().size(); ++i) {
            String url = createRouteUrl(DataManager.userLocation, DataManager.getPlaces().get(i).getLatLng(),
                    createViasExceptPlace(i), DataManager.getRouteParam().getTravelMode());
            RequestAsyncTask requestAsyncTask = new RequestAsyncTask();
            requestAsyncTask.execute(url, RouteRequestData.FIND_ROUTE, this, DataManager.getRouteParam().getTravelMode());
        }
    }

    private List<LatLng> createViasExceptPlace(int placeIdx) {
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < DataManager.getPlaces().size(); ++i) {
            if (i != placeIdx) {
                latLngs.add(DataManager.getPlaces().get(i).getLatLng());
            }
        }
        return latLngs;
    }

    public void findSectionRoute(LatLng origin, LatLng destination, TravelMode travelMode){
        String url = createSectionUrl(origin,destination,travelMode);
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask();
        requestAsyncTask.execute(url, RouteRequestData.FIND_SECTION, this, travelMode);
    }

    public void chooseTheBestRoute() {
        int minDuration = 1000000000;
        int minId = 0;
        List<Route> routes =  DataManager.getRouteRequestData().routes;
        for (int i = 0; i < DataManager.getRouteRequestData().routes.size(); ++i) {
            int currentDuration = DataManager.getRouteRequestData().routes.get(i).getDuration();
            if (currentDuration < minDuration) {
                minDuration = currentDuration;
                minId = i;
            }
        }
        Route route = DataManager.getRouteRequestData().routes.get(minId);
        setTravelTypeForSections(route, DataManager.getRouteParam().getTravelMode());
        DataManager.setRoute(route);
    }

    private void setTravelTypeForSections(Route route, TravelMode travelMode) {
        for (Section section : route.getSections()) {
            section.setTravelMode(travelMode);
        }
    }

    private String createRouteUrl(LatLng origin, LatLng destination, List<LatLng> vias, TravelMode travelMode) {
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=" + travelMode.name().toLowerCase();
        String waypoints = "waypoints=optimize:true";
        for (LatLng latLng : vias) {
            waypoints += "|" + latLng.latitude + "," + latLng.longitude;
        }
        String parameters = originStr + "&" + destinationStr + "&" + waypoints + "&" + sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private String createSectionUrl(LatLng origin, LatLng destination, TravelMode travelMode) {
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=" + travelMode.name().toLowerCase();
        String parameters = originStr + "&" + destinationStr + "&" +  sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private String createNearbyRestaurantNameUrl(String restaurantName, LatLng searchFromThisPlace, String radius_int) {
        String location = "location=" + searchFromThisPlace.latitude + "," + searchFromThisPlace.longitude;
        String output = "json";
        String radius = "radius="+radius_int;
        String keyword = "keyword="+restaurantName;
        return "maps.googleapis.com/maps/api/place/radarsearch/" + output + "?" + location +"&"+ radius +"&" + keyword;
    }

    private String createNearbyRestaurantUrl(LatLng searchFromThisPlace, String radius_int) {
        String location = "location=" + searchFromThisPlace.latitude + "," + searchFromThisPlace.longitude;
        String output = "json";
        String radius = "radius="+radius_int;
        String keyword = "keyword=restaurant";
        return "maps.googleapis.com/maps/api/place/radarsearch/" + output + "?" + location +"&"+ radius +"&" + keyword;
    }

    public MapsActivity getView() {
        return view;
    }
}
