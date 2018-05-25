package com.tripplanner.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz on 16.05.2018.
 */

public final class DataManager {

    private static List<Place> places = new ArrayList<>();

    public static void addPlace(Place place) {
        places.add(place);
    }

    public static void removePlace(int index) {
        places.remove(index);
    }

    public static List<Place> getPlaces(){
        return places;
    }

}
