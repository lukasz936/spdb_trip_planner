package com.tripplanner.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.media.MediaRouteSelector;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tripplanner.R;
import com.tripplanner.controller.MapsController;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.Route;
import com.tripplanner.model.Section;
import com.tripplanner.model.TravelMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.location.places.Place.TYPE_BAR;
import static com.google.android.gms.location.places.Place.TYPE_CAFE;
import static com.google.android.gms.location.places.Place.TYPE_FOOD;
import static com.google.android.gms.location.places.Place.TYPE_RESTAURANT;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private final int REQUEST_CODE_PLACEPICKER = 1;
    private GoogleMap mMap;
    private MapsController mapsController;
    private static final float ZOOM_POLAND = (float) 5.5;
    private static final float ZOOM_POINT = (float) 15;
    private static final float LAT_POLAND = (float) 52.03;
    private static final float LNG_POLAND = (float) 19.27;
    public static final int ADD_NEW_POSITION = 1;
    public static final int PREVIEW_POSITION = 2;
    public static final int SHOW_ROUTE = 3;
    public static final int ADD_NEW_LUNCH_PLACE = 4;
    private Map<TravelMode, String> travelModeDictionary = new HashMap<>();
    private List<Marker> listOfMarkers = new ArrayList<>();

    private com.tripplanner.model.Place currentPlace;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapsController = new MapsController(this);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mode = getIntent().getExtras().getInt("id");
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        if (mode == ADD_NEW_POSITION || mode == ADD_NEW_LUNCH_PLACE) {
            autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
                    if (mode == ADD_NEW_LUNCH_PLACE && !(place.getPlaceTypes().contains(TYPE_RESTAURANT) || place.getPlaceTypes().contains(TYPE_BAR) || place.getPlaceTypes().contains(TYPE_CAFE) || place.getPlaceTypes().contains(TYPE_FOOD))) {
                        new AlertDialog.Builder(MapsActivity.this)
                                .setMessage("W wybranym miejscu nic nie zjesz!")
                                .show();
                    } else {
                        currentPlace = new com.tripplanner.model.Place(place.getLatLng(), place.getName().toString());
                        mapsController.selectPlace(currentPlace, false);
                    }
                }

                @Override
                public void onError(Status status) {
                }
            });
        } else if (mode == PREVIEW_POSITION) {
            autocompleteFragment.getView().setVisibility(View.GONE);
        } else {
            findViewById(R.id.fabOpenRouteInfo).setVisibility(View.VISIBLE);
            autocompleteFragment.getView().setVisibility(View.GONE);
            mapsController.showRoute();
        }
        travelModeDictionary.put(TravelMode.DRIVING, "Samochód");
        travelModeDictionary.put(TravelMode.WALKING, "Pieszo");
        travelModeDictionary.put(TravelMode.TRANSIT, "Komunikacja miejska");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(LAT_POLAND, LNG_POLAND)).zoom(ZOOM_POLAND).build()));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location arg0) {
                    mapsController.updateLocation(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                }
            });
        }
        if (mode == ADD_NEW_POSITION || mode == ADD_NEW_LUNCH_PLACE) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    //placePicker
                    selectPoint(latLng, true);
                    startPlacePickerActivity(latLng);
                }
            });
        } else if (mode == PREVIEW_POSITION) {
            int placeId = getIntent().getExtras().getInt("placeId");
            mapsController.showPlace(placeId);
        }

        mMap.setOnMarkerClickListener(this);

    }

    public void selectPoint(LatLng latLng, boolean animate) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        if (animate) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).zoom(ZOOM_POINT).build()));
        }
    }

    @Override
    public void onBackPressed() {
        if (mode == ADD_NEW_POSITION) {
            if (currentPlace != null) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setMessage("Czy na pewno chcesz dodać wybrany punkt?")
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mapsController.cancelAddingPlace();
                            }
                        })
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mapsController.addPlace(currentPlace);
                            }
                        })
                        .show();
            } else {
                mapsController.cancelAddingPlace();
            }
        } else if (mode == ADD_NEW_LUNCH_PLACE) {
            if (currentPlace != null) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setMessage("Czy na pewno chcesz dodać wybraną restaurację?")
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mapsController.cancelAddingPlace();
                            }
                        })
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mapsController.addRestaurant(currentPlace);
                            }
                        })
                        .show();
            } else {
                mapsController.cancelAddingPlace();
            }
        } else {
            finish();
        }
    }

    private void startPlacePickerActivity(LatLng latLng) {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        intentBuilder.setLatLngBounds(new LatLngBounds(new LatLng(latLng.latitude - 0.005, latLng.longitude - 0.005),
                new LatLng(latLng.latitude + 0.005, latLng.longitude + 0.005)));
        // this would only work if you have your Google Places API working

        try {
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            Place placeSelected = PlacePicker.getPlace(data, this);
            if (mode == ADD_NEW_LUNCH_PLACE && !(placeSelected.getPlaceTypes().contains(TYPE_RESTAURANT) || placeSelected.getPlaceTypes().contains(TYPE_BAR) || placeSelected.getPlaceTypes().contains(TYPE_CAFE) || placeSelected.getPlaceTypes().contains(TYPE_FOOD))) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setMessage("W wybranym miejscu nic nie zjesz!")
                        .show();
            } else {
                currentPlace = new com.tripplanner.model.Place(placeSelected.getLatLng(), placeSelected.getName().toString());
                mapsController.selectPlace(currentPlace, true);
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void showRoute() {
        mMap.setMyLocationEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DataManager.userLocation, ZOOM_POINT));
        mMap.clear();
        for (int i = 0; i < DataManager.getPlaces().size(); ++i) {
            mMap.addMarker(new MarkerOptions().position(DataManager.getPlaces().get(i).getLatLng()));
        }
        PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.BLUE).width(13);
        for (Section section : DataManager.getRoute().getSections()) {
            for (List<LatLng> points : section.getPolylines()) {
                for (int i = 0; i < points.size(); ++i) {
                    polylineOptions.add(points.get(i));
                }
            }
        }
        mMap.addPolyline(polylineOptions);
    }


    public void openRouteInfo(View v) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String title = "Czas trasy: " + DataManager.getRoute().getDuration() / 60 + " h " + DataManager.getRoute().getDuration() % 60 + " min\n" +
                "Dystans: " + DataManager.getRoute().getDistance() / 1000 + " km " + DataManager.getRoute().getDistance() % 1000 + " m \n";
        ArrayList<String> infoList = new ArrayList<>();
        for (int i = 0; i < DataManager.getRoute().getSections().size(); ++i) {
            Section section = DataManager.getRoute().getSections().get(i);
            calendar.add(Calendar.MINUTE, section.getDuration());
            String sectionInfo = travelModeDictionary.get(section.getTravelMode()) +
                    "   " + section.getDuration() / 60 + " h " + section.getDuration() % 60 + " min" +
                    "   " + section.getDistance() / 1000 + " km " + section.getDistance() % 1000 + " m";
            String placeInfo = DataManager.getPlaces().get(i).getName() +
                    "   " + DataManager.getPlaces().get(i).getDuration() / 60 + " h " + DataManager.getPlaces().get(i).getDuration() % 60 + " min" +
                    "\n" + format.format(calendar.getTime()) + " - ";
            calendar.add(Calendar.MINUTE, DataManager.getPlaces().get(i).getDuration());
            placeInfo += format.format(calendar.getTime());
            infoList.add(sectionInfo);
            infoList.add(placeInfo);
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle(title);
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, infoList.toArray(new String[infoList.size()]));
        lv.setAdapter(adapter);
        alertDialog.show();
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        String[] items = new String[4];
        String travelMode_pl = "Pieszo";
        Section section = new Section();
        for (int i = 0; i < DataManager.getPlaces().size(); ++i) {
            if (marker.getPosition().latitude == DataManager.getPlaces().get(i).getLatLng().latitude && marker.getPosition().longitude == DataManager.getPlaces().get(i).getLatLng().longitude) {
                items[0] = DataManager.getPlaces().get(i).getName();
                section = DataManager.getRoute().getSections().get(i);
                switch (section.getTravelMode()) {
                    case DRIVING:
                        travelMode_pl = "Samochód";
                        break;
                    case TRANSIT:
                        travelMode_pl = "Komunikacja";
                        break;
                    case WALKING:
                        travelMode_pl = "Pieszo";
                        break;
                }
                items[1] = "Typ transport: " + travelMode_pl;
                items[2] = "Czas odcinka: " + section.getDuration() / 60 + " h " + section.getDuration() % 60 + " m ";
                items[3] = "Dystans: " + section.getDistance() / 1000 + " km " + section.getDistance() % 1000 + " m ";
                break;
            }
        }
        final Section sectionToChange = section;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informacje o odcinku trasy:");
        builder.setItems(items, null);
        String positive = "Pieszo";
        String negative = "Samochód";
        switch (sectionToChange.getTravelMode()) {
            case DRIVING: {
                positive = "Pieszo";
                negative = "Komunikacja";
                break;
            }
            case TRANSIT: {
                positive = "Pieszo";
                negative = "Samochód";
                break;
            }
            case WALKING: {
                positive = "Samochód";
                negative = "Komunikacja";
                break;
            }
        }
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (sectionToChange.getTravelMode()) {
                    case DRIVING: {
                        mapsController.changeSection(sectionToChange.getStartLocation(), sectionToChange.getEndLocation(),TravelMode.WALKING);
                        break;
                    }
                    case TRANSIT: {
                        mapsController.changeSection(sectionToChange.getStartLocation(), sectionToChange.getEndLocation(),TravelMode.WALKING);
                        break;
                    }
                    case WALKING: {
                        mapsController.changeSection(sectionToChange.getStartLocation(), sectionToChange.getEndLocation(),TravelMode.DRIVING);
                        break;
                    }
                }
            }
        });
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (sectionToChange.getTravelMode()) {
                    case DRIVING: {
                        mapsController.changeSection(sectionToChange.getStartLocation(), sectionToChange.getEndLocation(),TravelMode.TRANSIT);
                        break;
                    }
                    case TRANSIT: {
                        mapsController.changeSection(sectionToChange.getStartLocation(), sectionToChange.getEndLocation(),TravelMode.DRIVING);
                        break;
                    }
                    case WALKING: {
                        mapsController.changeSection(sectionToChange.getStartLocation(), sectionToChange.getEndLocation(),TravelMode.TRANSIT);
                        break;
                    }
                }
            }
        });
        builder.show();
        return false;
    }

}



