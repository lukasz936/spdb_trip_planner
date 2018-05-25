package com.tripplanner.controller;

/**
 * Created by Łukasz on 16.05.2018.
 */

public interface IMainController {

    void addLocalization();

    void setDuration(int placeId, int hours, int minutes);
}
