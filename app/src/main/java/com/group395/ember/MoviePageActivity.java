package com.group395.ember;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoviePageActivity extends AppCompatActivity {

    private static Movie currentMovie = null;
    //This field specifies where the user was directed from, and thus where the back button takes them.
    //If true, it takes them to HistoryActivity. If false, it takes them to
    private static boolean fromHistoryActivity = false;

    public static void setCurrentMovie(Movie input){
        currentMovie = input;
    }
    public static void setFromHistoryActivity(boolean input){ fromHistoryActivity = input; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);
        displayAll();
    }

    private void displayAll(){
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
        ImageView image = findViewById(R.id.imageView);
        Picasso.get().load(currentMovie.getPoster()).into(image);
    }

    protected void setMovie(Movie input){ currentMovie = input; }

    public void backOnClick(View v){
        if(fromHistoryActivity){
            startActivity(new Intent(MoviePageActivity.this, HistoryActivity.class));
        }else{
            startActivity(new Intent(MoviePageActivity.this, SearchResultsActivity.class));
        }
    }
    public void searchAgainOnClick(View v){ startActivity(new Intent(MoviePageActivity.this, SearchStartActivity.class)); }
}
