package com.group395.ember;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MoviePageActivity extends AppCompatActivity {

    private static Movie currentMovie = null;

    public static void setCurrentMovie(Movie input){
        currentMovie = input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);
        displayAll();
    }

    private void displayAll(){
        TextView titleText, yearText, genreText, runtimeText, actorText, writerText, directorText, metascoreText, imdbRatingText, plotText;
        titleText = findViewById(R.id.titleText);
        yearText = findViewById(R.id.yearText);
        genreText = findViewById(R.id.genreText);
        runtimeText = findViewById(R.id.runtimeText);
        actorText = findViewById(R.id.actorText);
        writerText = findViewById(R.id.writerText);
        directorText = findViewById(R.id.directorText);
        metascoreText = findViewById(R.id.metascoreText);
        imdbRatingText = findViewById(R.id.imdbRatingText);
        plotText = findViewById(R.id.plotText);
        titleText.setText(currentMovie.getTitle());
        yearText.setText(currentMovie.getYear().toString());
        genreText.setText(currentMovie.getGenre().toString());
        runtimeText.setText(currentMovie.getRuntime());
        actorText.setText(currentMovie.getActors().toString());
        writerText.setText(currentMovie.getWriter().toString());
        directorText.setText(currentMovie.getDirector());
        metascoreText.setText(currentMovie.getMetascore());
        imdbRatingText.setText(currentMovie.getImdbRating().toString());
        plotText.setText(currentMovie.getPlot());
    }

    protected void setMovie(Movie input){ currentMovie = input; }

    public void backOnClick(View v){ startActivity(new Intent(MoviePageActivity.this, SearchResultsActivity.class)); }
    public void searchAgainOnClick(View v){ startActivity(new Intent(MoviePageActivity.this, SearchStartActivity.class)); }
}
