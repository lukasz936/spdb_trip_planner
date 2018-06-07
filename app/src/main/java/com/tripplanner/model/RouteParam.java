package com.tripplanner.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class RouteParam {

    private List<Place> places = new ArrayList<>();

    private LunchOption lunchOption;

    private String restaurantName;

    private Place restaurant;

    private TravelMode travelMode;

    public RouteParam() {
        this.places = new ArrayList<>();
        this.restaurant = new Place(null, null);
        this.restaurant.setStartDate(new Date(2018, 1, 1, 13, 0));
        this.restaurant.setDuration(60);
        this.lunchOption = LunchOption.noPlace;
    }

    public void setLunchOption(LunchOption lunchOption) {
        this.lunchOption = lunchOption;
    }

    public Place getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Place restaurant) {
        this.restaurant = restaurant;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public LunchOption getLunchOption() {
        return lunchOption;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public Place getPlaceByLatLng(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        Place nearestPlace = null;
        float distance = 100000000;
        for (Place place : places) {
            Location tmpLocation = new Location("");
            tmpLocation.setLatitude(place.getLatLng().latitude);
            tmpLocation.setLongitude(place.getLatLng().longitude);
            float tmpDistance = location.distanceTo(tmpLocation);
            if (tmpDistance < distance) {
                distance = tmpDistance;
                nearestPlace = place;
            }
        }
        return nearestPlace;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public TravelMode getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(TravelMode travelMode) {
        this.travelMode = travelMode;
    }
}