package com.tripplanner.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz on 16.05.2018.
 */

public final class DataManager {

    private static List<Place> places = new ArrayList<>();

    public static void addPlace(Place place) {
        place.setId(places.size());
        places.add(place);
    }

    public static void removePlace(int id) {
        places.remove(id);
    }

    public static List<Place> getPlaces(){
        return places;
    }

    public static Place getPlace(int id){
        return places.get(id);
    }
}
