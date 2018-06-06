package com.tripplanner.controller.googleApi;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.Route;
import com.tripplanner.model.Section;
import com.tripplanner.model.TravelMode;
import com.tripplanner.view.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RequestAsyncTask extends AsyncTask {


    private MapsActivity view;

    @Override
    protected Object doInBackground(Object[] params) {
        String data = "";
        try {
            view = (MapsActivity) params[1];
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
        try {
            Route route = parseJson((String) o);
            DataManager.setRoute(route);
            view.showRoute(route);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ParserAsyncTask parserTask = new ParserAsyncTask();
        //parserTask.execute((String) o);
    }

    private Route parseJson(String data) throws JSONException {
        if (data == null) {
            return null;
        }
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        if (jsonRoutes.length() == 0) {
            return null;
        }

        int sumDuration = 0;
        JSONObject jsonRoute = jsonRoutes.getJSONObject(0);
        JSONObject jsonOverviewPolyline = jsonRoute.getJSONObject("overview_polyline");
        JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
        List<Section> sections = new ArrayList<>();
        for (int i = 0; i < jsonLegs.length(); ++i) {
            Section section = new Section();
            JSONObject jsonLeg = jsonLegs.getJSONObject(i);
            JSONObject jsonLegDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonLegDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonLegEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonLegStartLocation = jsonLeg.getJSONObject("start_location");
            JSONArray jsonSteps = jsonLeg.getJSONArray("steps");
            List<List<LatLng>> polylines = new ArrayList<>();
            for (int j = 0; j < jsonSteps.length(); ++j) {
                JSONObject jsonStep = jsonSteps.getJSONObject(j);
                JSONObject jsonStepPolyline = jsonStep.getJSONObject("polyline");
                polylines.add(decodePolyLine(jsonStepPolyline.getString("points")));
            }
            section.setDistance(jsonLegDistance.getInt("value"));
            section.setDuration(jsonLegDuration.getInt("value")/60);
            section.setEndLocation(new LatLng(jsonLegEndLocation.getDouble("lat"), jsonLegEndLocation.getDouble("lng")));
            section.setStartLocation(new LatLng(jsonLegStartLocation.getDouble("lat"), jsonLegStartLocation.getDouble("lng")));
            section.setPolylines(polylines);
            section.setTravelMode(TravelMode.WALKING);
            sections.add(section);
        }
        return new Route(sections, DataManager.getPlaces());
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(lat / 100000d, lng / 100000d));
        }
        return decoded;
    }
}

