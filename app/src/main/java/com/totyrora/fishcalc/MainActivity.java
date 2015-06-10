package com.totyrora.fishcalc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.security.Key;

public class MainActivity extends Activity
        implements AdapterView.OnItemSelectedListener {

    private Spinner spices;
    private Button calc;
    private EditText length;
    private TextView lengthText;
    private TextView weight;
    private Fish fish;
    private boolean lengthUnit = false;
    private boolean weightUnit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get app preferences
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);
        lengthUnit = prefs.getBoolean("fish_len_unit",false);
        weightUnit = prefs.getBoolean("fish_weight_unit",false);

        // Create Array and populate from xml
        String[] speciesArray;
        speciesArray = getResources().getStringArray(R.array.spices_array);

        // Create Adapter using the array, and assign a layout
        ArrayAdapter<String> adapterSpices;
        adapterSpices = new ArrayAdapter<String>(this, R.layout.spinner_item, speciesArray);
//        adapterSpices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Create a Spinner as defined in the layout, and assign the adapter
        spices = (Spinner) findViewById(R.id.spices_spinner);
        spices.setAdapter(adapterSpices);

        // Assign class to call on drop down selection
        spices.setOnItemSelectedListener(this);

        // Create EditText and associate to layout
        length = (EditText) findViewById(R.id.length_edit);
        lengthText = (TextView) findViewById(R.id.length_text);

        // Create a listener that update weight while typing
        length.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                getEditLength();
                displayWeight();
            }
        });

        // Create TextView and associate to layout
        weight = (TextView) findViewById(R.id.weight_view);

        // Create a fish and initialize kArray
        fish = new Fish(0, 0, getResources().getIntArray(R.array.k_array));

        // init result view
        displayWeight();
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Update preferences
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);
        lengthUnit = prefs.getBoolean("fish_len_unit",false);
        weightUnit = prefs.getBoolean("fish_weight_unit",false);

        // Update display
        displayWeight();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //call settings activity
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // update spices
        fish.setSpices(position);
        displayWeight();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

/*
        //TODO remove debug present data
        String lengthText = length.getText().toString();
        String tmpToast = "On Button : \n  Debug print \n"
                + spices.getItemAtPosition(fish.getSpices()).toString() + "\n"
                + lengthText;
        Toast.makeText(this, tmpToast, Toast.LENGTH_LONG).show();
    }
*/

    private void getEditLength() {
        String tmpStr = length.getText().toString();
        double convert;
        if (!tmpStr.isEmpty()) {
            //int tmpLen = Integer.parseInt(length.getText().toString());
            double tmpLenD = Double.parseDouble(length.getText().toString());
            if(lengthUnit) {
                convert = 2.54;
            } else {
                convert = 1.0;
            }
            if (tmpLenD*convert > 120) {tmpLenD=0.0;} // unrealistic
            fish.setLen(tmpLenD * convert);
        } else {
            fish.setLen(0);
        };
    }

    private void displayWeight() {

        if(lengthUnit) {
            lengthText.setText(getResources().getString(R.string.length_unit_inch));
        } else {
            lengthText.setText(getResources().getString(R.string.length_unit_cm));
        }

        String tmpUnit;
        double convert = 0;
        if (weightUnit){
            tmpUnit = getResources().getString(R.string.weight_unit_pound);
            convert = 2.2046;
        }else {
            tmpUnit = getResources().getString(R.string.weight_unit_kg);
            convert = 1.0;
        }

        String tmpWeight = String.format("%.2f", convert * fish.getWeight()/1000);
        String tmpWeightHi = String.format("%.2f", convert * fish.getWeightHi()/1000);
        String tmpWeightLo = String.format("%.2f", convert * fish.getWeightLo()/1000);

        String resultText = tmpWeightLo +" - <font color='#EE0000'>" + tmpWeight + "</font> - " +  tmpWeightHi + " " + tmpUnit;
        weight.setText(Html.fromHtml(resultText));
    }
}
