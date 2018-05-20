package com.tripplanner.controller;

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
}
