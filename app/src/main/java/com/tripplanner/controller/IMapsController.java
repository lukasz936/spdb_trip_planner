package com.tripplanner.controller;

import com.tripplanner.model.Place;

/**
 * Created by Łukasz on 20.05.2018.
 */

public interface IMapsController {

    void selectPlace(Place place, boolean clickedOnMap);
    void addPlace(Place place);
    void cancelAddingPlace();
    void showRoute();
    void showPlace(int placeId);
    void addRestaurant(Place place);
}
