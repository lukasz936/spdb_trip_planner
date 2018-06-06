package com.tripplanner.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private int distance;
    private int duration;
    private List<Section> sections = new ArrayList<>();
    private List<Place> places = new ArrayList<>();

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
        this.duration = duration;
    }
}
