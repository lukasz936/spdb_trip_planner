package com.tripplanner.controller;

import com.tripplanner.model.Place;
import com.tripplanner.view.MapsActivity;

/**
 * Created by Łukasz on 20.05.2018.
 */

public class MapController implements IMapController{

    private final MapsActivity view;

    public MapController(MapsActivity view) {
        this.view = view;
    }

    @Override
    public void selectPlace(Place place, boolean clikedOnMap) {
        if(clikedOnMap) {
            view.selectPoint(place.getLatLng(), false);
        } else {
            view.selectPoint(place.getLatLng(), true);
        }

    }


}
