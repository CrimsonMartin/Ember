package com.group395.ember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


public class SearchOptionsActivity extends AppCompatActivity {


    private Filter[] myFilters = new Filter[2];
    private FilterType selected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);
    }

    protected void genreOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter genre.");
        selected = FilterType.GENRE;
    }

    protected void actorOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter actor.");
        selected = FilterType.ACTOR;
    }

    protected void directorOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter director.");
        selected = FilterType.DIRECTOR;
    }

    protected void submitOnClick(View v){
        //If no FilterType has been selected, do nothing.
        if(selected == null){
            return;
        }
        EditText editText = findViewById(R.id.editText);
        //If the FilterType is already in myFilters, add the filter to that Filter.
        for(Filter tempFilter : myFilters){
            if(tempFilter.getFilterType().equals(selected)){
                tempFilter.add(editText.getText().toString());
            }
        }
        int size = myFilters.length;
        int firstEmpty = -1;
        //find first empty slot in arrays, if any
        for(int i = 0; i < size; i++){
            if(firstEmpty < 0){
                if(myFilters[i] == null){
                    firstEmpty = i;
                }
            }
        }
        //if arrays are full, double their size
        //probably only allow up to 8 or so, but that's something to do later
        if(firstEmpty < 0){
            Filter[] tempMyFilters = new Filter[myFilters.length * 2];
            for(int i = 0; i < myFilters.length; i++){
                tempMyFilters[i] = myFilters[i];
            }
            firstEmpty = myFilters.length;
            myFilters = tempMyFilters;
        }
        myFilters[firstEmpty] = new Filter(selected);
        String[] inputString = new String[1];
        inputString[0] = editText.getText().toString();
        switch(selected){
            case GENRE: myFilters[firstEmpty].setGenres(inputString);
            case ACTOR: myFilters[firstEmpty].setActors(inputString);
            case DIRECTOR: myFilters[firstEmpty].setDirectors(inputString);
        }
    }

    protected void resetOnClick(View v){
        myFilters = new Filter[2];
        selected = null;
        EditText editText = findViewById(R.id.editText);
        editText.setText("Choose a filter.");
        RadioGroup radioGroupGRD = findViewById(R.id.radioGroupGRD);
        RadioGroup radioGroupNAD = findViewById(R.id.radioGroupNAD);
        radioGroupGRD.clearCheck();
        radioGroupNAD.clearCheck();
    }

    protected void searchOnClick(View v){
        UISearch mySearch = new UISearch();


    }

}
