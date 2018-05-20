package com.tripplanner.controller;

import com.tripplanner.model.Place;

/**
 * Created by Łukasz on 20.05.2018.
 */

public interface IMapController {
    void selectPlace(Place place, boolean clikedOnMap);
}
