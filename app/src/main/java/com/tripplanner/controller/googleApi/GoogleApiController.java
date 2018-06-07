package com.tripplanner.controller.googleApi;

import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.LunchOption;
import com.tripplanner.model.Place;
import com.tripplanner.model.Route;
import com.tripplanner.model.RouteParam;
import com.tripplanner.model.RouteRequestData;
import com.tripplanner.model.Section;
import com.tripplanner.model.TravelMode;
import com.tripplanner.view.MapsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Łukasz on 27.05.2018.
 */

public class GoogleApiController {

    final private MapsActivity view;

    public GoogleApiController(MapsActivity view) {
        this.view = view;
    }

    public void findRoute() {
        if (DataManager.getPlaces().size() < 2) {
            Toast.makeText(view, "Nie wprowadzono wystarczającej liczby miejsc", Toast.LENGTH_LONG).show();
            return;
        }
        DataManager.setRouteRequestData(new RouteRequestData());
        RequestAsyncTask requestAsyncTask_1 = new RequestAsyncTask();
        requestAsyncTask_1.execute(createNearbyRestaurantUrl(new LatLng(52, 21), 5000), RouteRequestData.FIND_RESTAURANT, this, DataManager.getRouteParam().getTravelMode());

        for (int i = 0; i < DataManager.getPlaces().size(); ++i) {
            String url = createRouteUrl(DataManager.userLocation, DataManager.getPlaces().get(i).getLatLng(),
                    createViasExceptPlace(i), DataManager.getRouteParam().getTravelMode());
            RequestAsyncTask requestAsyncTask = new RequestAsyncTask();
            requestAsyncTask.execute(url, RouteRequestData.FIND_ROUTE, this, DataManager.getRouteParam().getTravelMode());
        }
    }

    private List<LatLng> createViasExceptPlace(int placeIdx) {
        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < DataManager.getPlaces().size(); ++i) {
            if (i != placeIdx) {
                latLngs.add(DataManager.getPlaces().get(i).getLatLng());
            }
        }
        return latLngs;
    }

    public void findSectionRoute(LatLng origin, LatLng destination, TravelMode travelMode) {
        String url = createSectionUrl(origin, destination, travelMode);
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask();
        requestAsyncTask.execute(url, RouteRequestData.FIND_SECTION, this, travelMode);
    }

    public void chooseTheBestRoute() {
        int minDuration = 1000000000;
        int minId = 0;
        List<Route> routes = DataManager.getRouteRequestData().routes;
        Route route;
        if (DataManager.getRouteParam().getLunchOption().equals(LunchOption.noPlace)) {
            for (int i = 0; i < DataManager.getRouteRequestData().routes.size(); ++i) {
                int currentDuration = DataManager.getRouteRequestData().routes.get(i).getDuration();
                if (currentDuration < minDuration) {
                    minDuration = currentDuration;
                    minId = i;
                }
            }
            route = DataManager.getRouteRequestData().routes.get(minId);
            setTravelTypeForSections(route, DataManager.getRouteParam().getTravelMode());
            DataManager.setRoute(route);
        } else if (DataManager.getRouteParam().getLunchOption().equals(LunchOption.exactPlace)) {
            int hourRequired = DataManager.getRouteParam().getRestaurant().getStartDate().getHours();
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < DataManager.getRouteRequestData().routes.size(); ++i) {
                Route currentRoute = DataManager.getRouteRequestData().routes.get(i);
                for (int j = 0; i < currentRoute.getSections().size(); ++j) {
                    calendar.add(Calendar.MINUTE, currentRoute.getSections().get(j).getDuration());
                    if (calendar.getTime().getHours() >= hourRequired) {
                        DataManager.getRouteRequestData().nearByRestaurantRoutes.put(i, j == 0 ? 0 : j - 1);
                        break;
                    }
                    calendar.add(Calendar.MINUTE, currentRoute.getPlaces().get(j).getDuration());
                }
            }
            int theNearestRouteIdx = 0;
            float minDistance = 100000000;
            Location restaurantLocation = new Location("");
            restaurantLocation.setLongitude(DataManager.getRouteParam().getRestaurant().getLatLng().longitude);
            restaurantLocation.setLatitude(DataManager.getRouteParam().getRestaurant().getLatLng().latitude);
            for (int i = 0; i < DataManager.getRouteRequestData().nearByRestaurantRoutes.size(); ++i) {
                Place tmpPlace = DataManager.getRouteRequestData().routes.get(i).getPlaces().get(DataManager.getRouteRequestData().nearByRestaurantRoutes.get(i));
                Location tmpLocation = new Location("");
                tmpLocation.setLatitude(tmpPlace.getLatLng().latitude);
                tmpLocation.setLongitude(tmpPlace.getLatLng().longitude);
                if (restaurantLocation.distanceTo(tmpLocation) < minDistance) {
                    minDistance = restaurantLocation.distanceTo(tmpLocation);
                    theNearestRouteIdx = i;
                }
            }
            route = DataManager.getRouteRequestData().routes.get(theNearestRouteIdx);
            setTravelTypeForSections(route, DataManager.getRouteParam().getTravelMode());
            DataManager.setRoute(route);
            addRestaurantToRoute(DataManager.getRouteParam().getRestaurant(), route, DataManager.getRouteRequestData().nearByRestaurantRoutes.get(theNearestRouteIdx));
        }
    }

    private void addRestaurantToRoute(Place restaurant, Route route, int afterPlaceIdx) {
        List<LatLng> vias = new ArrayList<>();
        vias.add(restaurant.getLatLng());
        DataManager.getRouteRequestData().afterPlaceIdx = afterPlaceIdx;
        String url = createRouteUrl(route.getPlaces().get(afterPlaceIdx).getLatLng(), route.getPlaces().get(afterPlaceIdx + 1).getLatLng(), vias, DataManager.getRouteParam().getTravelMode());
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask();
        requestAsyncTask.execute(url, RouteRequestData.FIND_ROUTE_RESTAURANT, this, DataManager.getRouteParam().getTravelMode());
    }

    public void addRouteToRoute(Route mainRoute, Route routeToAdd, int placeIdFrom) {
        List<Place> places = new ArrayList<>();
        List<Section> sections = new ArrayList<>();
        setTravelTypeForSections(routeToAdd, DataManager.getRouteParam().getTravelMode());
        for (int i = 0; i < mainRoute.getPlaces().size(); ++i) {
            if (placeIdFrom  == i) {
                places.add(mainRoute.getPlaces().get(i));
                sections.add(mainRoute.getSections().get(i));
                places.add(routeToAdd.getPlaces().get(0));
                sections.add(routeToAdd.getSections().get(0));
                places.add(routeToAdd.getPlaces().get(1));
                sections.add(routeToAdd.getSections().get(1));
                ++i;
            } else {
                places.add(mainRoute.getPlaces().get(i));
                sections.add(mainRoute.getSections().get(i));
            }
        }
        Route route = new Route(sections, places);
        DataManager.setRoute(route);
    }


    private void setTravelTypeForSections(Route route, TravelMode travelMode) {
        for (Section section : route.getSections()) {
            section.setTravelMode(travelMode);
        }
    }

    private String createRouteUrl(LatLng origin, LatLng destination, List<LatLng> vias, TravelMode travelMode) {
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=" + travelMode.name().toLowerCase();
        String waypoints = "waypoints=optimize:true";
        for (LatLng latLng : vias) {
            waypoints += "|" + latLng.latitude + "," + latLng.longitude;
        }
        String parameters = originStr + "&" + destinationStr + "&" + waypoints + "&" + sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private String createSectionUrl(LatLng origin, LatLng destination, TravelMode travelMode) {
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destinationStr = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=" + travelMode.name().toLowerCase();
        String parameters = originStr + "&" + destinationStr + "&" + sensor + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    private String createNearbyRestaurantNameUrl(String restaurantName, LatLng searchFromThisPlace, String radius_int) {
        String location = "location=" + searchFromThisPlace.latitude + "," + searchFromThisPlace.longitude;
        String output = "json";
        String radius = "radius=" + radius_int;
        String keyword = "keyword=" + restaurantName;
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + location + "&" + radius + "&" + keyword + "&key=AIzaSyBervaiJ4otUM0fKG4q-sXbF4zurIC1nck";
    }

    private String createNearbyRestaurantUrl(LatLng searchFromThisPlace, Integer radius_int) {
        String location = "location=" + searchFromThisPlace.latitude + "," + searchFromThisPlace.longitude;
        String output = "json";
        String radius = "radius=" + radius_int;
        String keyword = "keyword=restaurant";
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + location + "&" + radius + "&" + keyword + "&key=AIzaSyBervaiJ4otUM0fKG4q-sXbF4zurIC1nck";
    }

    public MapsActivity getView() {
        return view;
    }
}
