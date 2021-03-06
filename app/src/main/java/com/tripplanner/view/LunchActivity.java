package com.tripplanner.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tripplanner.R;
import com.tripplanner.controller.LunchController;
import com.tripplanner.model.DataManager;
import com.tripplanner.model.LunchOption;
import com.tripplanner.model.RouteParam;
//import com.tripplanner.model.RouteParam;
//import com.tripplanner.model.LunchOption;

public class LunchActivity extends AppCompatActivity {

    private LunchController controller;
    public boolean lunchPlaceSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);

        final TextView SetHourTitleTextView = (TextView) findViewById(R.id.textView_setHourTitle);
        SetHourTitleTextView.setText(R.string.set_hour_title);

        final TextView SetDurationTitleTextView = (TextView) findViewById(R.id.textView_setDuration);
        SetDurationTitleTextView.setText(R.string.set_duration_title);

        final TextView SetPlaceTitleTextView = (TextView) findViewById(R.id.textView_setPlace);
        SetPlaceTitleTextView.setText(R.string.set_place_title);


        RadioGroup radioGroup_Lunch = (RadioGroup) findViewById(R.id.radioGroup);

        RadioButton RadioButton_1=(RadioButton) findViewById(R.id.radioButton);
        RadioButton_1.setText(R.string.set_place_radio_button_1);

        RadioButton RadioButton_2=(RadioButton) findViewById(R.id.radioButton2);
        RadioButton_2.setText(R.string.set_place_radio_button_2);

        RadioButton RadioButton_3=(RadioButton) findViewById(R.id.radioButton3);
        RadioButton_3.setText(R.string.set_place_radio_button_3);

        RadioButton RadioButton_4=(RadioButton) findViewById(R.id.radioButton4);
        RadioButton_4.setText(R.string.set_place_radio_button_4);

        controller = new LunchController(this);

        FloatingActionButton StartLunchButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_StartLunchHour);
        StartLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(14,0,1);
            }
        });

        FloatingActionButton DurationLunchButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        DurationLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(1,0,2);
            }
        });

        TextView SetHourTextView = (TextView) findViewById(R.id.StartLunchTextView);
        SetHourTextView.setText(DataManager.getRouteParam().getRestaurant().getStartDate().toString().substring(11,16));


        TextView SetDurationTextView = (TextView) findViewById(R.id.LunchdurationTextView);
        SetDurationTextView.setText( String.valueOf(DataManager.getRouteParam().getRestaurant().getDuration()/60) + " h " + String.valueOf(DataManager.getRouteParam().getRestaurant().getDuration()%60) + " m " );
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked)
                    DataManager.getRouteParam().setLunchOption(LunchOption.exactPlace);
                    controller.startMapsActivity(MapsActivity.ADD_NEW_LUNCH_PLACE);
                break;
            case R.id.radioButton2:
                if (checked)
                    DataManager.getRouteParam().setLunchOption(LunchOption.placeType);
                    openPlacesListDialog();
                    break;
            case R.id.radioButton3:
                if (checked)
                    DataManager.getRouteParam().setLunchOption(LunchOption.anyPlace);
                    break;
            case R.id.radioButton4:
                if (checked)
                    DataManager.getRouteParam().setLunchOption(LunchOption.noPlace);
                break;
        }
    }


    public void openTimePicker(int currentHours, int currentMinutes, int id) {
        final Dialog dialog = new Dialog(LunchActivity.this);
        if(id==1)
            dialog.setTitle("Godzina rozpoczęcia");
        if(id==2)
            dialog.setTitle("Czas pobytu");
        dialog.setContentView(R.layout.time_picker_dialog);
        final NumberPicker numberPickerHour = (NumberPicker) dialog.findViewById(R.id.numberPickerHour);
        final NumberPicker numberPickerMin = (NumberPicker) dialog.findViewById(R.id.numberPickerMin);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(currentHours);
        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(59);
        numberPickerMin.setValue(currentMinutes);
        if(id == 1) {
            dialog.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   controller.setStartHour(numberPickerHour.getValue(), numberPickerMin.getValue());
                   TextView tv = (TextView) findViewById(R.id.StartLunchTextView);
                   tv.setText(DataManager.getRouteParam().getRestaurant().getStartDate().toString().substring(11,16));
                   dialog.dismiss();
                }
            });
        }
        if(id == 2){
            dialog.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.setDuration(numberPickerHour.getValue(), numberPickerMin.getValue());
                    TextView tv = (TextView) findViewById(R.id.LunchdurationTextView);
                    tv.setText( String.valueOf(DataManager.getRouteParam().getRestaurant().getDuration()/60) + " h " + String.valueOf(DataManager.getRouteParam().getRestaurant().getDuration()%60) + " m " );
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapsActivity.ADD_NEW_LUNCH_PLACE) {
            if (resultCode == Activity.RESULT_OK) {
                //lunchPlaceSelected = data.getData();
            }
        }
    }

    public void openPlacesListDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LunchActivity.this);
        builder.setTitle("Wybierz typ restauracji");
        final String[] restaurants = {"Burger King", "Da Grasso", "Dominium", "KFC", "McDonald's", "Pizza Hut", "Sphinx", "SUBWAY", "TelePizza"};
        final int checkedItem = 0;
        builder.setSingleChoiceItems(restaurants, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataManager.getRouteParam().setRestaurantName(restaurants[which]);
            }
        });

        builder.setPositiveButton("Wybierz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cofnij", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(DataManager.getRouteParam().getLunchOption() == LunchOption.exactPlace && DataManager.getRouteParam().getRestaurant().getLatLng() == null){
            new AlertDialog.Builder(LunchActivity.this)
                    .setMessage("Wybierz dokładne miejsce lunchu!")
                    .show();
        }
        else{
            super.onBackPressed();
        }
    }
}




