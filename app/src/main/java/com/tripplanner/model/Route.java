package com.tripplanner.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private int distance;
    private int duration;
    private List<Section> sections = new ArrayList<>();
    private List<Place> places = new ArrayList<>();

    public Route(){
    }

    public Route(List<Section> sections, List<Place> places) {
        this.sections = sections;
        this.places = places;
        updateDistance();
        updateDuration();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void updateDistance(){
        int distance = 0;
        for(Section section : sections){
            distance += section.getDistance();
        }
        this.distance = distance;
    }

    public void updateDuration(){
        int duration = 0;
        for(Section section : sections){
            duration += section.getDuration();
        }
        for(Place place : places){
            duration += place.getDuration();
        }
        this.duration = duration;
    }

    public Integer getPlaceIdxByLatLng(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        Integer nearestPlaceIdx = null;
        float distance = 100000000;
        for (int i =0; i< places.size(); ++i) {
            Location tmpLocation = new Location("");
            tmpLocation.setLatitude(places.get(i).getLatLng().latitude);
            tmpLocation.setLongitude(places.get(i).getLatLng().longitude);
            float tmpDistance = location.distanceTo(tmpLocation);
            if (tmpDistance < distance) {
                distance = tmpDistance;
                nearestPlaceIdx = i;
            }
        }
        return nearestPlaceIdx;
    }
}
