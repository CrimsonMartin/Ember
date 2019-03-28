package com.group395.ember;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MoviePageActivity extends AppCompatActivity {

    private static Movie currentMovie = null;

    public static void setCurrentMovie(Movie input){
        currentMovie = input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);
    }
}
