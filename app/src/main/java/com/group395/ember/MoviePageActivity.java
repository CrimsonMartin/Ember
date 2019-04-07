package com.group395.ember;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
//        TextView tempView = findViewById(R.id.titleText);
//        tempView.setText(currentMovie.getTitle());
//        tempView = findViewById(R.id.yearText);
//        tempView.setText(currentMovie.getYear().toString());
//        tempView = findViewById(R.id.genreText);
//        tempView.setText(getApplicationContext().getString(R.string.genre_list, SearchResultsActivity.stripBrackets(currentMovie.getGenre().toString())));
//        tempView = findViewById(R.id.runtimeText);
//        tempView.setText(getApplicationContext().getString(R.string.runtime, SearchResultsActivity.stripBrackets(currentMovie.getRuntime())));
//        tempView = findViewById(R.id.actorText);
//        tempView.setText(getApplicationContext().getString(R.string.actor_list, SearchResultsActivity.stripBrackets(currentMovie.getActors().toString())));
//        tempView = findViewById(R.id.writerText);
//        tempView.setText(getApplicationContext().getString(R.string.writer_list, SearchResultsActivity.stripBrackets(currentMovie.getWriter().toString())));
//        tempView = findViewById(R.id.directorText);
//        tempView.setText(getApplicationContext().getString(R.string.director_list, SearchResultsActivity.stripBrackets(currentMovie.getDirector())));
//        tempView = findViewById(R.id.metascoreText);
//        tempView.setText(getApplicationContext().getString(R.string.metascore, currentMovie.getMetascore()));
//        tempView = findViewById(R.id.imdbRatingText);
//        tempView.setText(getApplicationContext().getString(R.string.imdb_rating, currentMovie.getImdbRating().toString()));
//        tempView = findViewById(R.id.plotText);
//        tempView.setText(currentMovie.getPlot());
        Button tempDisp = findViewById(R.id.titleDisp);
        tempDisp.setText(currentMovie.getTitle());
        tempDisp = findViewById(R.id.yearDisp);
        tempDisp.setText(currentMovie.getYear().toString());
        tempDisp = findViewById(R.id.genreDisp);
        tempDisp.setText(getApplicationContext().getString(R.string.genre_list, SearchResultsActivity.stripBrackets(currentMovie.getGenre().toString())));
        tempDisp = findViewById(R.id.runtimeDisp);
        tempDisp.setText(getApplicationContext().getString(R.string.runtime, SearchResultsActivity.stripBrackets(currentMovie.getRuntime())));
        tempDisp = findViewById(R.id.actorDisp);
        tempDisp.setText(getApplicationContext().getString(R.string.actor_list, SearchResultsActivity.stripBrackets(currentMovie.getActors().toString())));
        tempDisp = findViewById(R.id.writerDisp);
        tempDisp.setText(getApplicationContext().getString(R.string.writer_list, SearchResultsActivity.stripBrackets(currentMovie.getWriter().toString())));
        tempDisp = findViewById(R.id.directorDisp);
        tempDisp.setText(getApplicationContext().getString(R.string.director_list, SearchResultsActivity.stripBrackets(currentMovie.getDirector())));
        tempDisp = findViewById(R.id.metascoreDisp);
        tempDisp.setText(getApplicationContext().getString(R.string.metascore, currentMovie.getMetascore()));
        tempDisp = findViewById(R.id.imdbRatingDisp);
        tempDisp.setText(getApplicationContext().getString(R.string.imdb_rating, currentMovie.getImdbRating().toString()));
        tempDisp = findViewById(R.id.plotDisp);
        tempDisp.setText(currentMovie.getPlot());
    }

    protected void setMovie(Movie input){ currentMovie = input; }

    public void backOnClick(View v){ startActivity(new Intent(MoviePageActivity.this, SearchResultsActivity.class)); }
    public void searchAgainOnClick(View v){ startActivity(new Intent(MoviePageActivity.this, SearchStartActivity.class)); }
}
