package com.tripplanner.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tripplanner.R;
import com.tripplanner.controller.MainController;
import com.tripplanner.model.DataManager;

public class MainActivity extends AppCompatActivity {

    private MainController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new MainController(this);
        controller.addLocalization();
        DataManager.elo();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void addLocalization(View v) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        //intent.putExtra("mode", "info_main");
        startActivity(intent);
        overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
    }
}
