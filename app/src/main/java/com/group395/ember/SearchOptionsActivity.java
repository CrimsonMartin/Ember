package com.group395.ember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.group395.ember.FilterType.ACTOR;
import static com.group395.ember.FilterType.DIRECTOR;
import static com.group395.ember.FilterType.GENRE;


public class SearchOptionsActivity extends AppCompatActivity {

    private int currentNumFilters = 0;
    private FilterHolder[] myFiltersLinear = new FilterHolder[5];
    private FilterType selected = null;
    private enum RadioFive{ RADIO_A, RADIO_B, RADIO_C, RADIO_D, RADIO_E }
    private class FilterHolder{
        private String text;
        private FilterType type;
        public FilterHolder(String inputText, FilterType inputType){
            text = inputText;
            type = inputType;
        }
        public String getText() { return text; }
        public FilterType getType(){ return type; }
        public String toString(){ return text; }
    }
    private RadioFive selectedRadio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);
    }

    public void genreOnClick(View v){
        EditText editText = findViewById(R.id.filterText);
        editText.setHint(R.string.edit_text_on_genre_click);
        selected = GENRE;
    }
    public void actorOnClick(View v){
        EditText editText = findViewById(R.id.filterText);
        editText.setHint(R.string.edit_text_on_actor_click);
        selected = ACTOR;
    }
    public void directorOnClick(View v){
        EditText editText = findViewById(R.id.filterText);
        editText.setHint(R.string.edit_text_on_director_click);
        selected = DIRECTOR;
    }

    public void radioAOnClick(View v){ selectedRadio = RadioFive.RADIO_A; }
    public void radioBOnClick(View v){ selectedRadio = RadioFive.RADIO_B; }
    public void radioCOnClick(View v){ selectedRadio = RadioFive.RADIO_C; }
    public void radioDOnClick(View v){ selectedRadio = RadioFive.RADIO_D; }
    public void radioEOnClick(View v){ selectedRadio = RadioFive.RADIO_E; }


    private void displayMessage(String message){
        Button display = findViewById(R.id.backButton);
        Button display2 = findViewById(R.id.resetButton);
        display.setText(message);
    }

    //TODO: Filters are not added once a filter is deleted.
    public void submitOnClick(View v){
        //If no FilterType has been selected, do nothing.
        if(selected == null){ return; }
        //If there's no more room for filters, do nothing.
        if (findFirstEmpty(myFiltersLinear) == -1) { return; }
        EditText editText = findViewById(R.id.filterText);
        String inputString = editText.getText().toString();
        //If the input box is empty, do nothing.
        if((inputString == null) || (inputString.equals(""))){ return; }
        int firstEmptyLinear = findFirstEmpty(myFiltersLinear);
        myFiltersLinear[firstEmptyLinear] = new FilterHolder(inputString, selected);
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
        displayRadio.setText(inputString);
        currentNumFilters++;
    }

    public void resetOnClick(View v){
        myFiltersLinear = new FilterHolder[5];
        selected = null;
        EditText editText = findViewById(R.id.filterText);
        editText.setHint(R.string.choose_filter);
        RadioGroup radioGroup = findViewById(R.id.radioGroupGAD);
        radioGroup.clearCheck();
        radioGroup = findViewById(R.id.radioGroupDisplay);
        radioGroup.clearCheck();
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
        EditText editText = findViewById(R.id.filterText);
        SearchResultsActivity.search(editText.getText().toString());
        startActivity(new Intent(SearchOptionsActivity.this, SearchResultsActivity.class));
    }

    public void deleteOnClick(View v){
        if(!checkRadioEmpty()) {
            if (selectedRadio != null) {
                RadioButton displayRadioA = findViewById(R.id.filterDisplayA);
                RadioButton displayRadioB = findViewById(R.id.filterDisplayB);
                RadioButton displayRadioC = findViewById(R.id.filterDisplayC);
                RadioButton displayRadioD = findViewById(R.id.filterDisplayD);
                RadioButton displayRadioE = findViewById(R.id.filterDisplayE);
                switch (selectedRadio) {
                    case RADIO_A:
                        displayRadioA.setText(displayRadioB.getText().toString());
                        displayRadioB.setText(displayRadioC.getText().toString());
                        displayRadioC.setText(displayRadioD.getText().toString());
                        displayRadioD.setText(displayRadioE.getText().toString());
                        displayRadioE.setText(null);
                        myFiltersLinear[0] = myFiltersLinear[1];
                        myFiltersLinear[1] = myFiltersLinear[2];
                        myFiltersLinear[2] = myFiltersLinear[3];
                        myFiltersLinear[3] = myFiltersLinear[4];
                        myFiltersLinear[4] = null;
                        break;
                    case RADIO_B:
                        displayRadioB.setText(displayRadioC.getText().toString());
                        displayRadioC.setText(displayRadioD.getText().toString());
                        displayRadioD.setText(displayRadioE.getText().toString());
                        displayRadioE.setText(null);
                        myFiltersLinear[1] = myFiltersLinear[2];
                        myFiltersLinear[2] = myFiltersLinear[3];
                        myFiltersLinear[3] = myFiltersLinear[4];
                        myFiltersLinear[4] = null;
                        break;
                    case RADIO_C:
                        displayRadioC.setText(displayRadioD.getText().toString());
                        displayRadioD.setText(displayRadioE.getText().toString());
                        displayRadioE.setText(null);
                        myFiltersLinear[2] = myFiltersLinear[3];
                        myFiltersLinear[3] = myFiltersLinear[4];
                        myFiltersLinear[4] = null;
                        break;
                    case RADIO_D:
                        displayRadioD.setText(displayRadioE.getText().toString());
                        displayRadioE.setText(null);
                        myFiltersLinear[3] = myFiltersLinear[4];
                        myFiltersLinear[4] = null;
                        break;
                    case RADIO_E:
                        displayRadioE.setText(null);
                        myFiltersLinear[4] = null;
                        break;
                }
                if (currentNumFilters > 0) {
                    currentNumFilters--;
                }
            }
        }
    }


    public void backOnClick(View v){ startActivity(new Intent(SearchOptionsActivity.this, SearchStartActivity.class)); }

    protected static int findFirstEmpty(Object[] input){
        for(int i = 0; i < input.length; i++){
            if(input[i] == null){
                return i;
            }
        }
        return -1;
    }
    private boolean checkRadioEmpty(){
        switch(selectedRadio){
            case RADIO_A:
                if(((RadioButton) findViewById(R.id.filterDisplayA)).getText().equals("")){ return true;}
                break;
            case RADIO_B:
                if(((RadioButton) findViewById(R.id.filterDisplayB)).getText().equals("")){ return true;}
                break;
            case RADIO_C:
                if(((RadioButton) findViewById(R.id.filterDisplayC)).getText().equals("")){ return true;}
                break;
            case RADIO_D:
                if(((RadioButton) findViewById(R.id.filterDisplayD)).getText().equals("")){ return true;}
                break;
            case RADIO_E:
                if(((RadioButton) findViewById(R.id.filterDisplayE)).getText().equals("")){ return true;}
                break;
        }
        return false;
    }
}

