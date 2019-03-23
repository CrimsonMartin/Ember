package com.group395.ember;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


public class SearchOptionsActivity extends AppCompatActivity {

    //allowed number of filters before extra filters will not be applied
    private final int allowableFilters = 5;
    private Filter[] myFilters = new Filter[allowableFilters];
    private FilterType selected = null;
    //contains IDs of extra textviews generated to display additional filters
    Integer[] genreIds = new Integer[allowableFilters];
    Integer[] actorIds = new Integer[allowableFilters];
    Integer[] directorIds = new Integer[allowableFilters];

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
        checkNumFilters();
        EditText editText = findViewById(R.id.editText);
        //If the FilterType is already in myFilters, add the filter to that Filter.
        for(int i = 0; i < myFilters.length; i++){
            if(myFilters[i] != null){
                if(myFilters[i].getFilterType().equals(selected)){
                    myFilters[i].add(editText.getText().toString());
                }
            }
        }
        //find first empty slot in arrays, if any. otherwise firstEmpty = -1
        int firstEmpty = findFirstEmpty(myFilters);
        myFilters[firstEmpty] = new Filter(selected);
        String[] inputString = new String[1];
        inputString[0] = editText.getText().toString();

        switch(selected){
            case GENRE: myFilters[firstEmpty].setGenres(inputString);
            case ACTOR: myFilters[firstEmpty].setActors(inputString);
            case DIRECTOR: myFilters[firstEmpty].setDirectors(inputString);
        }


        //add filter to display of filters
        TextView genreView = findViewById(R.id.genreFilters);
        genreIds[0] = R.id.genreFilters;
        TextView actorView = findViewById(R.id.actorFilters);
        actorIds[0] = R.id.genreFilters;
        TextView directorView = findViewById(R.id.directorFilters);
        directorIds[0] = R.id.directorFilters;
        switch(selected){
            case GENRE:
                if(genreView.getText().toString().equals(null)){
                    genreView.setText(editText.getText().toString());
                }else{
                    TextView genreViewAdditional = new TextView(this);
                    int idLocation = findFirstEmpty(genreIds);
                    genreIds[idLocation] = View.generateViewId();
                    genreViewAdditional.setId(genreIds[idLocation]);
                    genreViewAdditional.setText(editText.getText().toString());
                    ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                    constraintLayout.addView(genreViewAdditional);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(genreIds[idLocation].intValue(), ConstraintSet.TOP, genreIds[idLocation - 1].intValue(), ConstraintSet.BOTTOM, 16);
                    constraintSet.connect(genreIds[idLocation].intValue(), ConstraintSet.LEFT, R.id.radioGroupGAD, ConstraintSet.RIGHT, 16);
                    constraintSet.connect(genreIds[idLocation].intValue(), ConstraintSet.RIGHT, R.id.constraintLayout, ConstraintSet.RIGHT, 16);
                    constraintSet.connect(genreIds[idLocation].intValue(), ConstraintSet.BOTTOM, R.id.actorFilters, ConstraintSet.TOP, 16);
                    constraintSet.connect(actorView.getId(), ConstraintSet.TOP, genreIds[idLocation].intValue(), ConstraintSet.BOTTOM, 16);
                    constraintSet.applyTo(constraintLayout);
                }
            case ACTOR:
                if(actorView.getText().toString().equals(null)){
                    actorView.setText(editText.getText().toString());
                }else{
                    TextView actorViewAdditional = new TextView(this);
                    int idLocation = findFirstEmpty(actorIds);
                    actorIds[idLocation] = View.generateViewId();
                    actorViewAdditional.setId(View.generateViewId());
                    actorViewAdditional.setText(editText.getText().toString());
                    ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                    constraintLayout.addView(actorViewAdditional);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(actorIds[idLocation].intValue(), ConstraintSet.TOP, actorIds[idLocation - 1].intValue(), ConstraintSet.BOTTOM, 16);
                    constraintSet.connect(actorIds[idLocation].intValue(), ConstraintSet.LEFT, R.id.radioGroupGAD, ConstraintSet.RIGHT, 16);
                    constraintSet.connect(actorIds[idLocation].intValue(), ConstraintSet.RIGHT, R.id.constraintLayout, ConstraintSet.RIGHT, 16);
                    constraintSet.connect(actorIds[idLocation].intValue(), ConstraintSet.BOTTOM, R.id.directorFilters, ConstraintSet.TOP, 16);
                    constraintSet.connect(directorView.getId(), ConstraintSet.TOP, actorIds[idLocation].intValue(), ConstraintSet.BOTTOM, 16);
                    constraintSet.applyTo(constraintLayout);
                }
            case DIRECTOR:
                if(directorView.getText().toString().equals(null)){
                    directorView.setText(editText.getText().toString());
                }else{
                    TextView directorViewAdditional = new TextView(this);
                    int idLocation = findFirstEmpty(directorIds);
                    directorIds[idLocation] = View.generateViewId();
                    directorViewAdditional.setId(View.generateViewId());
                    directorViewAdditional.setText(editText.getText().toString());
                    ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                    constraintLayout.addView(directorViewAdditional);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(directorIds[idLocation].intValue(), ConstraintSet.TOP, directorIds[idLocation - 1].intValue(), ConstraintSet.BOTTOM, 16);
                    constraintSet.connect(directorIds[idLocation].intValue(), ConstraintSet.LEFT, R.id.radioGroupGAD, ConstraintSet.RIGHT, 16);
                    constraintSet.connect(directorIds[idLocation].intValue(), ConstraintSet.RIGHT, R.id.constraintLayout, ConstraintSet.RIGHT, 16);
                    constraintSet.connect(directorIds[idLocation].intValue(), ConstraintSet.BOTTOM, R.id.sendButton, ConstraintSet.TOP, 16);
                    constraintSet.connect(R.id.sendButton, ConstraintSet.TOP, directorIds[idLocation].intValue(), ConstraintSet.BOTTOM, 16);
                    constraintSet.applyTo(constraintLayout);
                }
        }


    }

    protected void resetOnClick(View v){
        myFilters = new Filter[2];
        selected = null;
        EditText editText = findViewById(R.id.editText);
        editText.setText("Choose a filter.");
        RadioGroup radioGroupGRD = findViewById(R.id.radioGroupGAD);
        radioGroupGRD.clearCheck();
    }

    protected void searchOnClick(View v){
        UISearch mySearch = new UISearch();

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
