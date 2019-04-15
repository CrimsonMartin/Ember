package com.group395.ember;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Objects.isNull;

public class SuggestionsActivity extends AppCompatActivity {

    private String title;
    private List<Movie> movies = new ArrayList<>();
    private AsyncTask<Void, Void, Void> loadSuggestionsTask;
    private List<View> suggestionButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("title");

        setContentView(R.layout.activity_suggestions);

        suggestionButtons.add(findViewById(R.id.suggestionA));
        suggestionButtons.add(findViewById(R.id.suggestionB));
        suggestionButtons.add(findViewById(R.id.suggestionC));
        suggestionButtons.add(findViewById(R.id.suggestionD));
        suggestionButtons.add(findViewById(R.id.suggestionE));
        suggestionButtons.add(findViewById(R.id.suggestionF));

        loadSuggestionsTask = new LoadSuggestionsTask();
        loadSuggestionsTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isNull(loadSuggestionsTask))
            loadSuggestionsTask.cancel(true);

        movies.clear();
    }

    private List<Movie> getMovies(int start, int end) throws IndexOutOfBoundsException{
        List<Movie> ret = new ArrayList<>();
        for(int i = start; i<end; i++) {
            ret.add(movies.get(i));
        }
        return ret;
    }

    private class LoadSuggestionsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            for (View v : suggestionButtons){
                ((Button)v).setText(null);
            }

            findViewById(R.id.nextButtonSA).setClickable(false);
            findViewById(R.id.previousButtonSA).setClickable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            List<Movie> moviesToBeDisplayed = getMovies(0,suggestionButtons.size());

            for(View v: suggestionButtons){
                ((Button)v).setText(
                        moviesToBeDisplayed.get(
                                suggestionButtons.indexOf(v))
                                .getTitle()
                );
            }

            findViewById(R.id.nextButtonSA).setClickable(true);
            findViewById(R.id.previousButtonSA).setClickable(true);

        }

        @Override
        protected Void doInBackground(Void... avoid) {

            List<Future<Movie>> loaded = new MovieSuggestions().setSuggestions(title);

            for (Future<Movie> future : loaded) {
                try {
                    movies.add(future.get(2, TimeUnit.SECONDS));

                } catch (TimeoutException e) {
                    //pass, try and load the next movie
                } catch(ExecutionException | InterruptedException e2){
                    //thread was interrupted, so return
                    return null;
                }
            }



            return null;
        }

    }


    public void suggestionOnClick(View v){
        Intent moviePageIntent = new Intent(SuggestionsActivity.this, MoviePageActivity.class);
        moviePageIntent.putExtra("title", ((Button)v).getText().toString());
        moviePageIntent.putExtra("fromHistoryView", false);
        startActivity(moviePageIntent);
    }

    public void nextOnClick(View v){
        //TODO
    }

    public void previousOnClick(View v){
        //TODO
    }


}

