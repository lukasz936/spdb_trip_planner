package com.tripplanner.controller.googleApi;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.tripplanner.model.Route;
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
            view.showRoute(parseJson((String) o));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //ParserAsyncTask parserTask = new ParserAsyncTask();
        //parserTask.execute((String) o);
    }

    private List<Route> parseJson(String data) throws JSONException {
        if (data == null) {
            return null;
        }
        List<Route> routes = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();

            JSONObject jsonOverviewPolyline = jsonRoute.getJSONObject("overview_polyline");
            route.points = decodePolyLine(jsonOverviewPolyline.getString("points"));
            routes.add(route);
        }
        return routes;
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

