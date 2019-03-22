package com.group395.ember;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
    }

    /**
     * @param searchText The name of the movie being searched.
     * @param myFilters The filters applied to the search.
     */
    protected void search(String searchText, Filter[] myFilters){

    }
}
