package com.tripplanner.controller;

import com.tripplanner.view.LunchActivity;

public class LunchController implements ILunchController {

    private final LunchActivity view;

    public LunchController(LunchActivity view) {
        this.view = view;
    }
}