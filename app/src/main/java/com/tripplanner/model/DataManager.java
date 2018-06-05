package com.tripplanner.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Łukasz on 16.05.2018.
 */

public final class DataManager {

    private static List<Place> places = new ArrayList<>();
    private static int maxPlaceId = 0;

    public static void addPlace(Place place) {
        place.setId(maxPlaceId);
        places.add(place);
        maxPlaceId++;
    }

    public static void removePlace(int id) {
        for (Iterator<Place> iterator = places.iterator(); iterator.hasNext();) {
            Place place = iterator.next();
            if (place.getId() == id) {
                iterator.remove();
            }
        }
    }

    public static List<Place> getPlaces() {
        return places;
    }

    public static Place getPlace(int id) {
        for (Iterator<Place> iterator = places.iterator(); iterator.hasNext();) {
            Place place = iterator.next();
            if (place.getId() == id) {
                return place;
            }
        }
        return null;
    }
}