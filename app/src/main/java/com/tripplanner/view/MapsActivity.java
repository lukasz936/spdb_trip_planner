package com.tripplanner.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tripplanner.R;
import com.tripplanner.controller.MapsController;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapsController mapsController;
    private static final float ZOOM_POLAND = (float) 5.5;
    private static final float ZOOM_POINT = (float) 15;
    private static final float LAT_POLAND = (float) 52.03;
    private static final float LNG_POLAND = (float) 19.27;
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
        if(mode == 1){
            autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    currentPlace = new com.tripplanner.model.Place(place.getLatLng(), place.getName().toString());
                    mapsController.selectPlace(currentPlace, false);
                }
                @Override
                public void onError(Status status) {
                }
            });
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
        if(mode == 1){
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    currentPlace = new com.tripplanner.model.Place(latLng);
                    mapsController.selectPlace(currentPlace, true);
                }
            });
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
        if(mode == 1){
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
        } else {
            finish();
        }
    }
}
