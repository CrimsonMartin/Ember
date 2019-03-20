package com.group395.ember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


public class SearchOptionsActivity extends AppCompatActivity {



    private String[] filters = new String[2];
    public enum GrdSelection {
        GENRE, RATING, DATE
    }
    private GrdSelection[] types = new GrdSelection[2];
    private GrdSelection selected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);
    }

    protected void searchOnClick(View v){
        startActivity(new Intent(SearchOptionsActivity.this, SearchActivity.class));
    }

    protected void genreOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter a genre filter.");
        selected = GrdSelection.GENRE;
    }

    protected void ratingOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter a rating filter.");
        selected = GrdSelection.RATING;
    }

    protected void dateOnClick(View v){
        EditText editText = findViewById(R.id.editText);
        editText.setText("Enter a date filter.");
        selected = GrdSelection.DATE;
    }

    protected void submitOnClick(View v){
        if(selected == null){
            return;
        }
        EditText editText = findViewById(R.id.editText);
        int size = filters.length;
        int firstEmpty = -1;
        //find first empty slot in arrays, if any
        for(int i = 0; i < size; i++){
            if(firstEmpty < 0){
                if(filters[i] == null){
                    firstEmpty = i;
                }
            }
        }
        //if arrays are full, double their size
        //probably only allow up to 8 or so, but that's something to do later
        if(firstEmpty < 0){
            String[] tempFilters = new String[filters.length * 2];
            GrdSelection[] tempTypes = new GrdSelection[types.length * 2];
            for(int i = 0; i < filters.length; i++){
                tempFilters[i] = filters[i];
                tempTypes[i] = types[i];
            }
            firstEmpty = filters.length;
            filters = tempFilters;
            types = tempTypes;
        }
        filters[firstEmpty] = editText.getText().toString();
        types[firstEmpty] = selected;

        //TEMPORARY, FOR THE DEMO

        TextView latestFilter = findViewById(R.id.latestFilter);
        latestFilter.setText(filters[firstEmpty]);
        switch(selected){
            case GENRE: genreOnClick(v);
            case RATING: ratingOnClick(v);
            case DATE: dateOnClick(v);
        }
    }

    protected void resetOnClick(View v){
        filters = new String[2];
        types = new GrdSelection[2];
        selected = null;
        EditText editText = findViewById(R.id.editText);
        editText.setText("Choose a filter above.");
        RadioGroup radioGroupGRD = findViewById(R.id.radioGroupGRD);
        RadioGroup radioGroupNAD = findViewById(R.id.radioGroupNAD);
        radioGroupGRD.clearCheck();
        radioGroupNAD.clearCheck();
    }
}
