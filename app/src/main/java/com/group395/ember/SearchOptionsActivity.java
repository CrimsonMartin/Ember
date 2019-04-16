package com.group395.ember;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.ArrayList;
import static com.group395.ember.FilterType.ACTOR;
import static com.group395.ember.FilterType.DIRECTOR;
import static com.group395.ember.FilterType.GENRE;


public class SearchOptionsActivity extends AppCompatActivity {

    private Filter[] myFilters = new Filter[]{ new Filter(GENRE), new Filter(ACTOR), new Filter(DIRECTOR) };
    private FilterType selected = null;
    private enum RadioThree{ RADIO_A, RADIO_B, RADIO_C }
    private RadioThree selectedRadio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_options);
    }

    /**
     * Linked to 'GENRE' radio button on Filters page.
     */
    public void genreOnClick(View v){
        EditText editText = findViewById(R.id.filterText);
        editText.setHint(R.string.edit_text_on_genre_click);
        selected = GENRE;
    }
    /**
     * Linked to 'ACTOR' radio button on Filters page.
     */
    public void actorOnClick(View v){
        EditText editText = findViewById(R.id.filterText);
        editText.setHint(R.string.edit_text_on_actor_click);
        selected = ACTOR;
    }
    /**
     * Linked to 'DIRECTOR' radio button on Filters page.
     */
    public void directorOnClick(View v){
        EditText editText = findViewById(R.id.filterText);
        editText.setHint(R.string.edit_text_on_director_click);
        selected = DIRECTOR;
    }

    public void radioAOnClick(View v){ selectedRadio = RadioThree.RADIO_A; }
    public void radioBOnClick(View v){ selectedRadio = RadioThree.RADIO_B; }
    public void radioCOnClick(View v){ selectedRadio = RadioThree.RADIO_C; }

    /**
     * Linked to 'ADD FILTER' button on Filters page.
     */
    //TODO: Filters are not added once a filter is deleted.
    public void submitOnClick(View v){
        //If no FilterType has been selected, do nothing.
        if(selected == null){ return; }
        EditText editText = findViewById(R.id.filterText);
        String inputString = editText.getText().toString();
        //If the input box is empty, do nothing.
        if(inputString.equals("")){ return; }
        Log.e("Ember", selected.toString());
        //add filter to display of filters
        RadioButton displayRadio = findViewById(R.id.filterDisplayA);
        ArrayList<String> arrList = new ArrayList<>(1);
        arrList.add(inputString);
        switch(selected) {
            case GENRE:
                myFilters[0].setGenres(arrList);
                displayRadio = findViewById(R.id.filterDisplayA);
                break;
            case ACTOR:
                myFilters[1].setActors(arrList);
                displayRadio = findViewById(R.id.filterDisplayB);
                break;
            case DIRECTOR:
                myFilters[2].setDirectors(arrList);
                displayRadio = findViewById(R.id.filterDisplayC);
                break;
        }
        displayRadio.setText(inputString);
    }

    /**
     * Linked to the "RESET" button on the filters page.
     */
    public void resetOnClick(View v){
        myFilters = new Filter[]{ new Filter(GENRE), new Filter(ACTOR), new Filter(DIRECTOR) };
        selected = null;
        ((EditText) findViewById(R.id.filterText)).setHint(R.string.choose_filter);
        ((RadioGroup) findViewById(R.id.radioGroupGAD)).clearCheck();
        ((RadioGroup) findViewById(R.id.radioGroupDisplay)).clearCheck();
        ((Button) findViewById(R.id.filterDisplayA)).setText(null);
        ((Button) findViewById(R.id.filterDisplayB)).setText(null);
        ((Button) findViewById(R.id.filterDisplayC)).setText(null);
    }

    /**
     * Linked to the "ACCEPT FILTERS" button on the filters page.
     */
    public void acceptOnClick(View v){
        String lastSearch = SearchResultsActivity.uiSearch.getSearch();
        Boolean actorNotTitle = SearchResultsActivity.uiSearch.getActorNotTitle();
        SearchResultsActivity.uiSearch = new UISearch();
        SearchResultsActivity.uiSearch.setSearch(lastSearch);
        // Assuming that it's an array of length 3 (3 unique filters)...
        for (int i = 0; i < 3; i++) {
            if (myFilters[i] != null) {
                SearchResultsActivity.uiSearch.addFilter(myFilters[i]);
            }
        }

        Intent intent = new Intent(SearchOptionsActivity.this, SearchResultsActivity.class);
        intent.putExtra("searchInput", lastSearch);
        intent.putExtra("actorNotTitle", actorNotTitle); //TODO: some way to retain actorNotTitle post-search...
        intent.putExtra("newSearch", false);
        startActivity(intent);
    }

    /**
     * Linked to the "Delete" button on the filters page. Removes the current Filter from the selected radio button
     * @param v
     */
    public void deleteOnClick(View v){
        if((selectedRadio != null) && !checkRadioEmpty()) {
            switch (selectedRadio) {
                case RADIO_A:
                    ((RadioButton) findViewById(R.id.filterDisplayA)).setText(null);
                    myFilters[0] = new Filter(GENRE);
                    break;
                case RADIO_B:
                    ((RadioButton) findViewById(R.id.filterDisplayB)).setText(null);
                    myFilters[1] = new Filter(ACTOR);
                    break;
                case RADIO_C:
                    ((RadioButton) findViewById(R.id.filterDisplayC)).setText(null);
                    myFilters[2] = new Filter(DIRECTOR);
                    break;
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
        }
        return false;
    }
}

