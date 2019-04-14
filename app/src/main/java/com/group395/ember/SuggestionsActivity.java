package com.group395.ember;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SuggestionsActivity extends AppCompatActivity {
    private String title = getIntent().getStringExtra("title");
    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
    }

    public Movie[] getSuggestions (Integer pagesSkipped){
        MovieSuggestions suggestions = new MovieSuggestions();
        suggestions.setSuggestions(title);
        int moviesNeeded = 0;
        moviesNeeded = pagesSkipped > 0 ? 10 : 5;
        while(movies.size() < moviesNeeded){
            try {
                Movie current = suggestions.results.poll(2, TimeUnit.SECONDS);
                if (current != null)
                    movies.add(current);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Movie[] displayed = new Movie[5];
        if(moviesNeeded>5) {
            for (int i = 0; i < displayed.length; i++)
                displayed[i] = movies.get(i + 5);
        }
        else{
            for (int i = 0; i < displayed.length; i++)
                displayed[i] = movies.get(i);
        }
        return displayed;
    }
}
