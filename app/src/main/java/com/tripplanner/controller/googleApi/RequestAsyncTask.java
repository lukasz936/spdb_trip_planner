package com.tripplanner.controller.googleApi;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.RouteRequestData;
import com.tripplanner.model.Section;
import com.tripplanner.model.TravelMode;
import com.tripplanner.view.MapsActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RequestAsyncTask extends AsyncTask {

    private int requestType;
    private GoogleApiController googleApiController;
    private TravelMode travelMode;

    @Override
    protected Object doInBackground(Object[] params) {
        String data = "";
        try {
            travelMode = (TravelMode) params[3];
            googleApiController = (GoogleApiController) params[2];
            requestType = (int) params[1];
            data = sendUrl((String) params[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    private String sendUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer strBuffer = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                strBuffer.append(line);
            }
            data = strBuffer.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (requestType == RouteRequestData.FIND_ROUTE) {
            DataManager.getRouteRequestData().requestCounter++;
            DataManager.getRouteRequestData().routes.add(JsonDataParser.parseRouteResponse((String) o));
            if (DataManager.getRouteRequestData().requestCounter == DataManager.getPlaces().size()) {
                googleApiController.chooseTheBestRoute();
                googleApiController.getView().showRoute();
            }
        } else if (requestType == RouteRequestData.FIND_SECTION) {
            Section newSection = JsonDataParser.parseSectionRouteResponse((String) o);
            newSection.setTravelMode(travelMode);
            for (int i = 0; i < DataManager.getRoute().getSections().size(); ++i) {
                if (DataManager.getRoute().getSections().get(i).getStartLocation().latitude == newSection.getStartLocation().latitude &&
                        DataManager.getRoute().getSections().get(i).getStartLocation().longitude == newSection.getStartLocation().longitude) {
                    DataManager.getRoute().getSections().set(i,newSection);
                }
            }
            DataManager.getRoute().updateDuration();
            DataManager.getRoute().updateDistance();
            googleApiController.getView().showRoute();
        } else if(requestType == RouteRequestData.FIND_RESTAURANT){
            List<LatLng> newRestaurantsList = JsonDataParser.parseSearchResponse((String) o);
            DataManager.getRouteRequestData().restaurantsList = newRestaurantsList;
        }
    }

}

