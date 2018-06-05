package com.tripplanner.controller;

/**
 * Created by Łukasz on 16.05.2018.
 */

public interface IMainController {

    void setDuration(int placeId, int hours, int minutes);
    void startMapsActivity(int id, Integer placeId);
    void removePlace(int placeId);
    void startLunchActivity(int id);
}
