package com.tripplanner.model;

import java.util.List;
import java.util.Date;

public final class RouteParam {

    List<Place> places;

    LunchOption lunchOption = LunchOption.anyPlace;

    String restaurantName;

    Place restaurant;

    public RouteParam(List<Place> places) {
        this.places = places;
        this.restaurant = new Place(null, null);
        this.restaurant.setStartDate(new Date(2018, 1,1, 13, 0));
        this.restaurant.setDuration(60);
    }

    public  void setLunchOption(LunchOption lunchOption_1){lunchOption = lunchOption_1;}

    public  Place getRestaurant(){return  restaurant;}

    public  void setRestaurant(Place  restaurant_1){restaurant = restaurant_1;}

    public  void setRestaurantName(String  restaurantName_1){restaurantName = restaurantName_1;}

    public LunchOption getLunchOption() { return  lunchOption;}
}