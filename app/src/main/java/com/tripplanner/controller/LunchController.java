package com.tripplanner.controller;

import android.content.Intent;

import com.tripplanner.R;
import com.tripplanner.view.LunchActivity;
import com.tripplanner.view.MapsActivity;

public class LunchController implements ILunchController {

    private final LunchActivity view;

    public LunchController(LunchActivity view) {
        this.view = view;
    }

    @Override
    public void startMapsActivity(int id) {
        Intent i = new Intent(view, MapsActivity.class);
        i.putExtra("id", id);
        view.startActivityForResult(i, id);
        view.overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
    }
}
