package com.tripplanner.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.tripplanner.R;
import com.tripplanner.controller.MainController;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.Place;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainController controller;
    RelativeLayout placeListLayout;
    private int i = 0;

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
        startActivityForResult(new Intent(this, MapsActivity.class), 1);
        overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                addViewItem(DataManager.getPlaces().get(DataManager.getPlaces().size() - 1).getName());
            }
        }
    }

    public void addViewItem(String text) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View viewToAdd = inflater.inflate(R.layout.activity_main_row, null, false);
        viewToAdd.setId(DataManager.getPlaces().size() + 10000001);
        ((TextView) viewToAdd.findViewById(R.id.activityMainRowText)).setText(text);
        viewToAdd.findViewById(R.id.activityMainRowButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                setDuration();
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 150);
        if (DataManager.getPlaces().size() == 0) {
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            params.addRule(RelativeLayout.BELOW, DataManager.getPlaces().size() + 10000000);
        }
        params.setMargins(1, 5, 1, 5);
        viewToAdd.setLayoutParams(params);
        placeListLayout.addView(viewToAdd);
    }

    public void setDuration(){

    }
}
