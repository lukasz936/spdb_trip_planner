package com.tripplanner.model;

import java.util.List;

public final class RouteParam {

    List<Place> places;

    static LunchOption lunchOption = LunchOption.anyPlace;

    String restaurantName;

    static Place restaurant;

    public static void setLunchOption(LunchOption lunchOption_1){lunchOption = lunchOption_1;}

    public static Place getRestaurant(){return  restaurant;}
}