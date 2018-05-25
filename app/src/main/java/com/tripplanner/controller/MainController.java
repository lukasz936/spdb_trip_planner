package com.tripplanner.controller;

import com.tripplanner.model.DataManager;
import com.tripplanner.view.MainActivity;

/**
 * Created by Łukasz on 16.05.2018.
 */

public class MainController implements IMainController {

    private final MainActivity view;

    public MainController(MainActivity view) {
        this.view = view;
    }

    @Override
    public void addLocalization() {
    }

    @Override
    public void setDuration(int placeId, int hours, int minutes) {
        DataManager.getPlace(placeId).setDuration(hours * 60 + minutes);
        view.setDurationLabel(placeId, hours, minutes);
    }
}
