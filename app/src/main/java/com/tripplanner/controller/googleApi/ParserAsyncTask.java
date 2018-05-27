package com.tripplanner.controller.googleApi;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Łukasz on 27.05.2018.
 */

public class ParserAsyncTask extends AsyncTask<String, Integer, List<List<HashMap>>> {

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            //DirectionsJSONParser parser = new DirectionsJSONParser();

            //routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap>> result) {
        ArrayList points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();


    }
}