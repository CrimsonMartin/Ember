package com.group395.ember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.group395.ember.FilterType.ACTOR;
import static com.group395.ember.FilterType.DIRECTOR;
import static com.group395.ember.FilterType.GENRE;


public class SearchOptionsActivity extends AppCompatActivity {


    private int currentNumFilters = 0;
    private Filter[] myFilters = new Filter[3];
    private FilterType selected = null;
    private enum RadioFive{
        RADIO_A, RADIO_B, RADIO_C, RADIO_D, RADIO_E
    }
    private RadioFive selectedRadio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);
    }

    public void genreOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText(R.string.edit_text_on_genre_click);
        selected = GENRE;
    }

    public void actorOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText(R.string.edit_text_on_actor_click);
        selected = ACTOR;
    }

    public void directorOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText(R.string.edit_text_on_director_click);
        selected = DIRECTOR;
    }

    public void radioAOnClick(View v){
        selectedRadio = RadioFive.RADIO_A;
    }
    public void radioBOnClick(View v){
        selectedRadio = RadioFive.RADIO_B;
    }
    public void radioCOnClick(View v){
        selectedRadio = RadioFive.RADIO_C;
    }
    public void radioDOnClick(View v){
        selectedRadio = RadioFive.RADIO_D;
    }
    public void radioEOnClick(View v){
        selectedRadio = RadioFive.RADIO_E;
    }

    public void submitOnClick(View v){
        //If no FilterType has been selected, do nothing.
        if(selected == null){
            return;
        }
        EditText editText = findViewById(R.id.editText);
        boolean inserted = false;
        //If the FilterType is already in myFilters, add the filter to that Filter.
        for(int i = 0; i < 3; i++){
            if(myFilters[i] != null){
                if((!inserted) && (myFilters[i].getFilterType().equals(selected))){
                    myFilters[i].add(editText.getText().toString());
                    inserted = true;
                }
            }
        }
        //If there is no Filter of the proper FilterType, make a new one.
        if(!inserted) {
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

    public void resetOnClick(View v){
        myFilters = new Filter[3];
        selected = null;
        EditText editText = findViewById(R.id.editText);
        editText.setText(R.string.choose_filter);
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

    public void searchOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        SearchResultsActivity.search(editText.getText().toString(), myFilters);
        startActivity(new Intent(SearchOptionsActivity.this, SearchResultsActivity.class));
    }

    public void deleteOnClick(View v){
        RadioButton displayRadioA = findViewById(R.id.filterDisplayA);
        RadioButton displayRadioB = findViewById(R.id.filterDisplayB);
        RadioButton displayRadioC = findViewById(R.id.filterDisplayC);
        RadioButton displayRadioD = findViewById(R.id.filterDisplayD);
        RadioButton displayRadioE = findViewById(R.id.filterDisplayE);
        switch(selectedRadio){
            case RADIO_A:
                displayRadioA.setText(displayRadioB.getText().toString());
                displayRadioB.setText(displayRadioC.getText().toString());
                displayRadioC.setText(displayRadioD.getText().toString());
                displayRadioD.setText(displayRadioE.getText().toString());
                displayRadioE.setText(null);
                break;
            case RADIO_B:
                displayRadioB.setText(displayRadioC.getText().toString());
                displayRadioC.setText(displayRadioD.getText().toString());
                displayRadioD.setText(displayRadioE.getText().toString());
                displayRadioE.setText(null);
                break;
            case RADIO_C:
                displayRadioC.setText(displayRadioD.getText().toString());
                displayRadioD.setText(displayRadioE.getText().toString());
                displayRadioE.setText(null);

                break;
            case RADIO_D:
                displayRadioD.setText(displayRadioE.getText().toString());
                displayRadioE.setText(null);
                break;
            case RADIO_E:
                displayRadioE.setText(null);
                break;
        }
        if(currentNumFilters > 0){
            currentNumFilters--;
        }
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
