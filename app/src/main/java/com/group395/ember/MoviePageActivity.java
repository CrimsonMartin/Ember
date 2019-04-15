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
        if(currentMovie != null) {
            if ((currentMovie.getYear() != null) && (currentMovie.getYear() > 0)) {
                ((Button) findViewById(R.id.titleDisp)).setText(getApplicationContext().getString(R.string.title_with_year, currentMovie.getTitle(), currentMovie.getYear().toString()));
            } else {
                ((Button) findViewById(R.id.titleDisp)).setText(currentMovie.getTitle());
            }
            if ((currentMovie.getGenre() != null) && (currentMovie.getGenre().size() > 0)) {
                ((Button) findViewById(R.id.genreDisp)).setText(getApplicationContext().getString(R.string.genre_list, SearchResultsActivity.stripBrackets(currentMovie.getGenre().toString())));
            } else {
                ((Button) findViewById(R.id.genreDisp)).setText(R.string.no_genres);
            }
            if ((currentMovie.getActors() != null) && (currentMovie.getActors().size() > 0)) {
                ((Button) findViewById(R.id.actorDisp)).setText(getApplicationContext().getString(R.string.actor_list, SearchResultsActivity.stripBrackets(currentMovie.getActors().toString())));
            } else {
                ((Button) findViewById(R.id.actorDisp)).setText(R.string.no_actors);
            }
            if ((currentMovie.getWriter() != null) && (currentMovie.getWriter().size() > 0)) {
                ((Button) findViewById(R.id.writerDisp)).setText(getApplicationContext().getString(R.string.writer_list, SearchResultsActivity.stripBrackets(currentMovie.getWriter().toString())));
            } else {
                ((Button) findViewById(R.id.writerDisp)).setText(R.string.no_writers);
            }
            if (currentMovie.getDirector() != null) {
                ((Button) findViewById(R.id.directorDisp)).setText(getApplicationContext().getString(R.string.director_list, (currentMovie.getDirector())));
            } else {
                ((Button) findViewById(R.id.directorDisp)).setText(R.string.no_director);
            }
            ((Button) findViewById(R.id.metascoreDisp)).setText(getApplicationContext().getString(R.string.metascore, currentMovie.getMetascore()));
            if (currentMovie.getImdbRating() == null) {
                ((Button) findViewById(R.id.imdbRatingDisp)).setText(R.string.no_imdb);
            } else {
                ((Button) findViewById(R.id.imdbRatingDisp)).setText(getApplicationContext().getString(R.string.imdb_rating, currentMovie.getImdbRating().toString()));
            }
            if(currentMovie.getPlot() != null) {
                ((Button) findViewById(R.id.plotDisp)).setText(currentMovie.getPlot());
            }else{
                ((Button) findViewById(R.id.plotDisp)).setText(R.string.no_plot);
            }
            if((currentMovie.getPlatforms() != null) && (currentMovie.getPlatforms().size() > 0)){
                ((Button) findViewById(R.id.platformDisp)).setText(getApplicationContext().getString(R.string.platforms, SearchResultsActivity.stripBrackets(currentMovie.getPlatforms().toString())));
            }else{
                ((Button) findViewById(R.id.platformDisp)).setText(R.string.no_platform);
            }
            if(currentMovie.getPoster() != null) {
                Uri uri = Uri.parse(currentMovie.getPoster());
                SimpleDraweeView draweeView = findViewById(R.id.imageViewDrawee);
                draweeView.setImageURI(uri);
            }
        }
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
