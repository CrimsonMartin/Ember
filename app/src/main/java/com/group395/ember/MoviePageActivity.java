package com.group395.ember;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MoviePageActivity extends AppCompatActivity {

    private Movie currentMovie = null;
    //This field specifies where the user was directed from, and thus where the back button takes them.
    //If true, it takes them to HistoryActivity. If false, it takes them to
    private boolean fromHistoryActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Future<Movie> currentMovieFuture = null;

        currentMovie = new Movie(getIntent().getStringExtra("title"));
        currentMovie.setImdbID(getIntent().getStringExtra("id"));
        fromHistoryActivity = getIntent().getBooleanExtra("fromHistoryActivity", false);

        try{

            //TODO this needs to be an async activity that loads the movie and turns on the display when it's loaded

            MovieLoader ml = new MovieLoader();
            currentMovieFuture = ml.loadMovie(currentMovie);
            currentMovie = currentMovieFuture.get();
        }catch(InterruptedException | ExecutionException e){
            //pass
        }

        setContentView(R.layout.activity_movie_page);
        displayAll();
    }

    private void displayAll(){
        Button tempDisp = findViewById(R.id.titleDisp);
        tempDisp.setText(currentMovie.getTitle());
        tempDisp = findViewById(R.id.yearDisp);
        if(currentMovie.getYear() > 0) { tempDisp.setText(currentMovie.getYear().toString()); }
        else{ tempDisp.setText(R.string.no_year); }
        tempDisp = findViewById(R.id.genreDisp);
        if(currentMovie.getGenre().size() > 0){ tempDisp.setText(getApplicationContext().getString(R.string.genre_list, SearchResultsActivity.stripBrackets(currentMovie.getGenre().toString()))); }
        else{ tempDisp.setText(R.string.no_genres);}
        tempDisp = findViewById(R.id.runtimeDisp);
        tempDisp.setText(currentMovie.getRuntime());
        tempDisp = findViewById(R.id.actorDisp);
        if(currentMovie.getActors().size() > 0){ tempDisp.setText(getApplicationContext().getString(R.string.actor_list, SearchResultsActivity.stripBrackets(currentMovie.getActors().toString()))); }
        else{ tempDisp.setText(R.string.no_actors); }
        tempDisp = findViewById(R.id.writerDisp);
        if(currentMovie.getWriter().size() > 0){ tempDisp.setText(getApplicationContext().getString(R.string.writer_list, SearchResultsActivity.stripBrackets(currentMovie.getWriter().toString()))); }
        else{ tempDisp.setText(R.string.no_writers); }
        tempDisp = findViewById(R.id.directorDisp);
        if(currentMovie.getDirector() != null){ tempDisp.setText(getApplicationContext().getString(R.string.director_list, (currentMovie.getDirector()))); }
        else{ tempDisp.setText(R.string.no_director); }
        tempDisp = findViewById(R.id.metascoreDisp);
        tempDisp.setText(getApplicationContext().getString(R.string.metascore, currentMovie.getMetascore()));
        tempDisp = findViewById(R.id.imdbRatingDisp);
        if(currentMovie.getImdbRating() == null){ tempDisp.setText("No IMDb rating found"); }
        else{ tempDisp.setText(getApplicationContext().getString(R.string.imdb_rating, currentMovie.getImdbRating().toString())); }
        tempDisp = findViewById(R.id.plotDisp);
        tempDisp.setText(currentMovie.getPlot());
        Uri uri = Uri.parse(currentMovie.getPoster());
        SimpleDraweeView draweeView = findViewById(R.id.imageViewDrawee);
        draweeView.setImageURI(uri);
    }

    protected void setMovie(Movie input){ currentMovie = input; }

    public void backOnClick(View v){
        if(fromHistoryActivity){
            startActivity(new Intent(MoviePageActivity.this, HistoryActivity.class));
        }
        else{
            startActivity(new Intent(MoviePageActivity.this, SearchResultsActivity.class));
        }
    }

    public void suggestionsOnClick(View v){
        Intent suggestionsIntent = new Intent(MoviePageActivity.this, SuggestionsActivity.class);
        suggestionsIntent.putExtra("title", currentMovie.getTitle());
        startActivity(suggestionsIntent);
    }
}
