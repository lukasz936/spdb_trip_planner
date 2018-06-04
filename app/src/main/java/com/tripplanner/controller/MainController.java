package com.tripplanner.controller;

import android.content.Intent;

import com.tripplanner.R;
import com.tripplanner.model.DataManager;
import com.tripplanner.view.LunchActivity;
import com.tripplanner.view.MainActivity;
import com.tripplanner.view.MapsActivity;

/**
 * Created by Łukasz on 16.05.2018.
 */

public class MainController implements IMainController {

    private final MainActivity view;

    public MainController(MainActivity view) {
        this.view = view;
    }

    @Override
    public void setDuration(int placeId, int hours, int minutes) {
        DataManager.getPlace(placeId).setDuration(hours * 60 + minutes);
        view.setDurationLabel(placeId, hours, minutes);
    }

    @Override
    public void startMapsActivity(int id) {
        Intent i = new Intent(view, MapsActivity.class);
        i.putExtra("id", id);
        view.startActivityForResult(i, id);
        view.overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
    }

    @Override
    public void startLunchActivity(int id) {
        Intent i = new Intent(view, LunchActivity.class);
        i.putExtra("id", id);
        view.startActivityForResult(i, id);
        //view.overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
    }
}
