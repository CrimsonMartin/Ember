package com.group395.ember;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.group395.ember.FilterType.ACTOR;
import static com.group395.ember.FilterType.DIRECTOR;
import static com.group395.ember.FilterType.GENRE;


public class SearchOptionsActivity extends AppCompatActivity {


    private int currentNumFilters = 0;
    private Filter[] myFilters = new Filter[3];
    private FilterType selected = null;
    private enum RadioFive{
        RADIO_A, RADIO_B, RADIO_C, RADIO_D, RADIO_E;
    }
    private RadioFive selectedRadio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);
    }

    protected void genreOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter genre.");
        selected = GENRE;
    }

    protected void actorOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter actor.");
        selected = ACTOR;
    }

    protected void directorOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter director.");
        selected = DIRECTOR;
    }

    protected void radioAOnClick(View v){
        selectedRadio = RadioFive.RADIO_A;
    }
    protected void radioBOnClick(View v){
        selectedRadio = RadioFive.RADIO_B;
    }
    protected void radioCOnClick(View v){
        selectedRadio = RadioFive.RADIO_C;
    }
    protected void radioDOnClick(View v){
        selectedRadio = RadioFive.RADIO_D;
    }
    protected void radioEOnClick(View v){
        selectedRadio = RadioFive.RADIO_E;
    }

    protected void submitOnClick(View v){
        //If no FilterType has been selected, do nothing.
        if(selected == null){
            return;
        }
        checkNumFilters();
        EditText editText = findViewById(R.id.editText);
        boolean inserted = false;
        //If the FilterType is already in myFilters, add the filter to that Filter.
        for(int i = 0; i < 3; i++){
            if(myFilters[i] != null){
                if((inserted == false) && (myFilters[i].getFilterType().equals(selected))){
                    myFilters[i].add(editText.getText().toString());
                    inserted = true;
                }
            }
        }
        //If there is no Filter of the proper FilterType, make a new one.
        if(inserted == false) {
            //find first empty slot in arrays, if any. otherwise firstEmpty = -1
            int firstEmpty = findFirstEmpty(myFilters);
            myFilters[firstEmpty] = new Filter(selected);
            String[] inputString = new String[1];
            inputString[0] = editText.getText().toString();

            switch (selected) {
                case GENRE:
                    myFilters[firstEmpty].setGenres(inputString);
                    break;
                case ACTOR:
                    myFilters[firstEmpty].setActors(inputString);
                    break;
                case DIRECTOR:
                    myFilters[firstEmpty].setDirectors(inputString);
                    break;
            }
        }

        //add filter to display of filters
        RadioButton displayRadio = findViewById(R.id.filterDisplayA);
        switch(currentNumFilters){
            case 0: displayRadio = findViewById(R.id.filterDisplayA);
                    break;
            case 1: displayRadio = findViewById(R.id.filterDisplayB);
                    break;
            case 2: displayRadio = findViewById(R.id.filterDisplayC);
                    break;
            case 3: displayRadio = findViewById(R.id.filterDisplayD);
                    break;
            case 4: displayRadio = findViewById(R.id.filterDisplayE);
                    break;
        }
        displayRadio.setText(editText.getText().toString());
        currentNumFilters++;


    }

    protected void resetOnClick(View v){
        myFilters = new Filter[3];
        selected = null;
        EditText editText = findViewById(R.id.editText);
        editText.setText("Choose a filter.");
        RadioGroup radioGroupGRD = findViewById(R.id.radioGroupGAD);
        radioGroupGRD.clearCheck();
        RadioButton displayRadio = findViewById(R.id.filterDisplayA);
        displayRadio.setText(null);
        displayRadio = findViewById(R.id.filterDisplayB);
        displayRadio.setText(null);
        displayRadio = findViewById(R.id.filterDisplayC);
        displayRadio.setText(null);
        displayRadio = findViewById(R.id.filterDisplayD);
        displayRadio.setText(null);
        displayRadio = findViewById(R.id.filterDisplayE);
        displayRadio.setText(null);
        currentNumFilters = 0;
    }

    protected void searchOnClick(View v){
        UISearch mySearch = new UISearch();

    }

    protected void deleteOnClick(View v){
        RadioButton displayRadio;
        RadioButton displayRadioTemp;
        switch(selectedRadio){
            case RADIO_A:
                displayRadio = findViewById(R.id.filterDisplayA);
                displayRadioTemp = findViewById(R.id.filterDisplayB);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadio = findViewById(R.id.filterDisplayB);
                displayRadioTemp = findViewById(R.id.filterDisplayC);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadio = findViewById(R.id.filterDisplayC);
                displayRadioTemp = findViewById(R.id.filterDisplayD);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadio = findViewById(R.id.filterDisplayD);
                displayRadioTemp = findViewById(R.id.filterDisplayE);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadioTemp.setText(null);
                break;
            case RADIO_B:
                displayRadio = findViewById(R.id.filterDisplayB);
                displayRadioTemp = findViewById(R.id.filterDisplayC);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadio = findViewById(R.id.filterDisplayC);
                displayRadioTemp = findViewById(R.id.filterDisplayD);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadio = findViewById(R.id.filterDisplayD);
                displayRadioTemp = findViewById(R.id.filterDisplayE);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadioTemp.setText(null);
                break;
            case RADIO_C:
                displayRadio = findViewById(R.id.filterDisplayC);
                displayRadioTemp = findViewById(R.id.filterDisplayD);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadio = findViewById(R.id.filterDisplayD);
                displayRadioTemp = findViewById(R.id.filterDisplayE);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadioTemp.setText(null);
                break;
            case RADIO_D:
                displayRadio = findViewById(R.id.filterDisplayD);
                displayRadioTemp = findViewById(R.id.filterDisplayE);
                displayRadio.setText(displayRadioTemp.getText().toString());
                displayRadioTemp.setText(null);
                break;
            case RADIO_E:
                displayRadio = findViewById(R.id.filterDisplayE);
                displayRadio.setText(null);
                break;
        }
        if(currentNumFilters > 0){
            currentNumFilters--;
        }
    }


    private void checkNumFilters(){

    }

    private int findFirstEmpty(Object[] input){
        for(int i = 0; i < input.length; i++){
            if(input[i] == null){
                return i;
            }
        }
        return -1;
    }

}
