package com.tripplanner.controller;

import android.app.Activity;
import android.content.Intent;

import com.tripplanner.model.DataManager;
import com.tripplanner.model.Place;
import com.tripplanner.view.MapsActivity;

/**
 * Created by Łukasz on 20.05.2018.
 */

public class MapsController implements IMapsController {

    private final MapsActivity view;

    public MapsController(MapsActivity view) {
        this.view = view;
    }

    @Override
    public void selectPlace(Place place, boolean clickedOnMap) {
        if(clickedOnMap) {
            view.selectPoint(place.getLatLng(), false);
        } else {
            view.selectPoint(place.getLatLng(), true);
        }
    }

    @Override
    public void addPlace(Place place){
        DataManager.addPlace(place);
        view.setResult(Activity.RESULT_OK, new Intent());
        view.finish();
    }

    @Override
    public void cancelAddingPlace(){
        view.setResult(Activity.RESULT_CANCELED, new Intent());
        view.finish();
    }

}
