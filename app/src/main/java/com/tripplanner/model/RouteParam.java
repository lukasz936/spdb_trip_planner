package com.tripplanner.model;

import java.util.List;

public final class RouteParam {

    List<Place> places;

    static LunchOption lunchOption;

    String restaurantName;

    Place restaurant;

    public static void setLunchOption(LunchOption lunchOption_1){lunchOption = lunchOption_1;}

}