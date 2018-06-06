package com.tripplanner.controller;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.tripplanner.R;
import com.tripplanner.model.DataManager;
import com.tripplanner.view.LunchActivity;
import com.tripplanner.view.MainActivity;
import com.tripplanner.view.MapsActivity;
/**
 * Created by Łukasz on 16.05.2018.
 */

public class MainController implements IMainController {

    private final MainActivity view;

    public MainController(MainActivity view) {
        this.view = view;
    }

    @Override
    public void setDuration(int placeId, int hours, int minutes) {
        DataManager.getPlaceById(placeId).setDuration(hours * 60 + minutes);
        view.setDurationLabel(placeId, hours, minutes);
    }

    @Override
    public void startMapsActivity(int id, Integer placeId) {
        Intent i = new Intent(view, MapsActivity.class);
        i.putExtra("id", id);
        if (id == MapsActivity.PREVIEW_POSITION && placeId != null) {
            i.putExtra("placeId", placeId);
        }
        if( id == MapsActivity.SHOW_ROUTE || id == MapsActivity.ADD_NEW_POSITION){
            if(id == MapsActivity.SHOW_ROUTE && DataManager.userLocation == null){
                Toast.makeText(view, "Lokalizacja użytkownika nie jest określona", Toast.LENGTH_LONG).show();
                return;
            }
            if(!isGpsOn()){
                Toast.makeText(view, "Aplikacja wymaga lokalizacji użytkownika. Proszę włączyć GPS", Toast.LENGTH_LONG).show();
                return;
            }
        }
        view.startActivityForResult(i, id);
        view.overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
    }

    @Override
    public void startLunchActivity(int id) {
        Intent i = new Intent(view, LunchActivity.class);
        i.putExtra("id", id);
        view.startActivityForResult(i, id);
        view.overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
    }

    @Override
    public void removePlace(int placeId) {
        MainActivity.views.get(placeId).setVisibility(View.GONE);
        DataManager.removePlaceById(placeId);
    }

    public boolean isGpsOn(){
        try {
            int off = Settings.Secure.getInt(view.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return off != 0;

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
