package com.tripplanner.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.android.gms.vision.text.Text;
import com.tripplanner.R;
import com.tripplanner.controller.MainController;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.Place;
import com.tripplanner.model.TravelMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainController controller;
    private RelativeLayout placeListLayout;
    public static List<View> views = new ArrayList<>();
    private static final int VIEW_ID_OFFSET = 1000000;
    boolean mLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        placeListLayout = (RelativeLayout) findViewById(R.id.mainPlaceList);
        controller = new MainController(this);
        getLocationPermission();
        DataManager.init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {
                    finish();
                }
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addLocalization(View v) {
        controller.startMapsActivity(MapsActivity.ADD_NEW_POSITION, null);
    }

    public void addLunch(View v) {
        controller.startLunchActivity(MapsActivity.ADD_NEW_LUNCH_PLACE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                addViewItem(DataManager.getPlaces().get(DataManager.getPlaces().size() - 1));
            }
        }
    }

    public void addViewItem(final Place place) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.activity_main_row, null, false);
        view.setId(DataManager.getMaxPlaceId() + VIEW_ID_OFFSET);
        ((TextView) view.findViewById(R.id.activityMainRowText)).setText(place.getName());
        view.findViewById(R.id.activityMainRowButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                openMultiOptionWindow(place.getId());
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 150);
        if (DataManager.getPlaces().size() == 0) {
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            params.addRule(RelativeLayout.BELOW, DataManager.getMaxPlaceId() + VIEW_ID_OFFSET - 1);
        }
        params.setMargins(1, 5, 1, 5);
        view.setLayoutParams(params);
        views.add(view);
        placeListLayout.addView(view);
    }

    public void openMultiOptionWindow(final int placeId) {
        CharSequence options[] = new CharSequence[]{"Czas pobytu", "Podgląd na mapie", "Usuń"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wybierz");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openTimePicker(placeId, 1, 0);
                        break;
                    case 1:
                        controller.startMapsActivity(MapsActivity.PREVIEW_POSITION, placeId);
                        break;
                    case 2:
                        controller.removePlace(placeId);
                }
            }
        });
        builder.show();
    }

    public void openTimePicker(final int placeId, int currentHours, int currentMinutes) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Czas pobytu");
        dialog.setContentView(R.layout.time_picker_dialog);
        final NumberPicker numberPickerHour = (NumberPicker) dialog.findViewById(R.id.numberPickerHour);
        final NumberPicker numberPickerMin = (NumberPicker) dialog.findViewById(R.id.numberPickerMin);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(24);
        numberPickerHour.setValue(currentHours);
        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(60);
        numberPickerMin.setValue(currentMinutes);
        dialog.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setDuration(placeId, numberPickerHour.getValue(), numberPickerMin.getValue());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void setDurationLabel(int placeId, int hours, int minutes) {
        ((TextView) views.get(placeId).findViewById(R.id.activityMainRowHours)).setText(String.valueOf(hours));
        ((TextView) views.get(placeId).findViewById(R.id.activityMainRowMins)).setText(String.valueOf(minutes));
    }

    public void showRoute(View v) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Wybierz rodzaj transportu");
        final String[] restaurants = {"Pieszo", "Samochód", "Komunikacja miejska"};
        final int checkedItem = 0;
        builder.setSingleChoiceItems(restaurants, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        DataManager.getRouteParam().setTravelMode(TravelMode.WALKING);
                        break;
                    case 1:
                        DataManager.getRouteParam().setTravelMode(TravelMode.DRIVING);
                        break;
                    case 2:
                        DataManager.getRouteParam().setTravelMode(TravelMode.TRANSIT);
                        break;
                }

            }
        });
        builder.setPositiveButton("Wybierz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.startMapsActivity(MapsActivity.SHOW_ROUTE, null);
            }
        });
        builder.setNegativeButton("Cofnij", null);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

}
