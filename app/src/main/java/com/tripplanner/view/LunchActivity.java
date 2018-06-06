package com.tripplanner.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tripplanner.R;
import com.tripplanner.controller.LunchController;
//import com.tripplanner.model.RouteParam;
//import com.tripplanner.model.LunchOption;

public class LunchActivity extends AppCompatActivity {

    private LunchController controller;

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

        controller = new LunchController(this);
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked)
                    //RouteParam.setLunchOption(LunchOption.exactPlace);
                    controller.startMapsActivity(1);
                break;
            case R.id.radioButton2:
                if (checked)
                    //RouteParam.setLunchOption(LunchOption.placeType);
                    break;
            case R.id.radioButton3:
                if (checked)
                    //RouteParam.setLunchOption(LunchOption.anyPlace);
                    break;
        }
    }

}
