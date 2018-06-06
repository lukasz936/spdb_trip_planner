package com.tripplanner.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Łukasz on 16.05.2018.
 */

public final class DataManager {

    private static List<Place> places = new ArrayList<>();
    private static int maxPlaceId = 0;
    public static LatLng userLocation;

    public static void init() {
        addPlace(new Place(new LatLng(52.2325521, 21.0061136), "xdd1"));
        addPlace(new Place(new LatLng(52.5325521, 21.2061136), "xdd2"));
        addPlace(new Place(new LatLng(52.8325521, 21.4061136), "xdd3"));
        addPlace(new Place(new LatLng(52.2675521, 21.8061136), "xdd4"));
        addPlace(new Place(new LatLng(52.445941, 20.993381), "xdd5"));
        addPlace(new Place(new LatLng(52.176502, 21.066162), "xdd6"));
    }

    public static void addPlace(Place place) {
        place.setId(maxPlaceId);
        places.add(place);
        maxPlaceId++;
    }

    public static List<Place> getPlaces() {
        return places;
    }

    public static void removePlaceById(int id) {
        for (Iterator<Place> iterator = places.iterator(); iterator.hasNext(); ) {
            Place place = iterator.next();
            if (place.getId() == id) {
                iterator.remove();
            }
        }
    }

    public static Place getPlaceById(int id) {
        for (Iterator<Place> iterator = places.iterator(); iterator.hasNext(); ) {
            Place place = iterator.next();
            if (place.getId() == id) {
                return place;
            }
        }
        return null;
    }

    public static int getMaxPlaceId() {
        return maxPlaceId;
    }


}