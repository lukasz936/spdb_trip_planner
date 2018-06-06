package com.tripplanner.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.media.MediaRouteSelector;
import android.view.View;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tripplanner.R;
import com.tripplanner.controller.MapsController;
import com.tripplanner.model.Route;

import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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
                    currentPlace = new com.tripplanner.model.Place(place.getLatLng(), place.getName().toString());
                    mapsController.selectPlace(currentPlace, false);
                }

                @Override
                public void onError(Status status) {
                }
            });
        } else if (mode == PREVIEW_POSITION) {
            autocompleteFragment.getView().setVisibility(View.GONE);
        } else {
            autocompleteFragment.getView().setVisibility(View.GONE);
            mapsController.showRoute();
        }
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
        }
        else if(mode == ADD_NEW_LUNCH_PLACE){
            if (currentPlace != null){
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
        }
        else {
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

            currentPlace = new com.tripplanner.model.Place(placeSelected.getLatLng(), placeSelected.getName().toString());
            mapsController.selectPlace(currentPlace, true);
        }
    }

    public void showRoute(List<Route> routes) {
        for (Route route : routes) {
           /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);*/

           /*mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));*/

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            mMap.addPolyline(polylineOptions);
        }
    }
}
