package com.tripplanner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class RouteParam {

    private List<Place> places = new ArrayList<>();

    private LunchOption lunchOption = LunchOption.anyPlace;

    private String restaurantName;

    private Place restaurant;

    private TravelMode travelMode;

    public RouteParam() {
        this.places = new ArrayList<>();
        this.restaurant = new Place(null, null);
        this.restaurant.setStartDate(new Date(2018, 1,1, 13, 0));
        this.restaurant.setDuration(60);
    }

    public  void setLunchOption(LunchOption lunchOption_1){lunchOption = lunchOption_1;}

    public  Place getRestaurant(){return  restaurant;}

    public  void setRestaurant(Place  restaurant){restaurant = restaurant;}

    public  void setRestaurantName(String  restaurantName){restaurantName = restaurantName;}

    public LunchOption getLunchOption() { return  lunchOption;}

    public List<Place> getPlaces() {
        return places;
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