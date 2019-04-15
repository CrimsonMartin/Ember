package com.group395.ember;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPage = 1;
        title = getIntent().getStringExtra("title");

        setContentView(R.layout.activity_suggestions);

        suggestionButtons.add(findViewById(R.id.suggestionA));
        suggestionButtons.add(findViewById(R.id.suggestionB));
        suggestionButtons.add(findViewById(R.id.suggestionC));
        suggestionButtons.add(findViewById(R.id.suggestionD));
        suggestionButtons.add(findViewById(R.id.suggestionE));
        suggestionButtons.add(findViewById(R.id.suggestionF));

        updatePageNumber();

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

    private class LoadSuggestionsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setAllSuggestionsTo(View.INVISIBLE);

            findViewById(R.id.nextButtonSA).setClickable(false);
            findViewById(R.id.previousButtonSA).setClickable(false);
            findViewById(R.id.suggestionProgressBar).setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setAllSuggestionsTo(View.VISIBLE);

            try{
                updateMoviesByPageNumber();
            } catch (IndexOutOfBoundsException e){
                createNoMoviesAlertDialog();
            }

            findViewById(R.id.nextButtonSA).setClickable(true);
            findViewById(R.id.previousButtonSA).setClickable(true);
            findViewById(R.id.suggestionProgressBar).setVisibility(View.INVISIBLE);

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

    private void createNoMoviesAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("No suggested movies could be found");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void suggestionOnClick(View v){
        Intent moviePageIntent = new Intent(SuggestionsActivity.this, MoviePageActivity.class);
        moviePageIntent.putExtra("title", ((Button)v).getText().toString());
        moviePageIntent.putExtra("fromHistoryView", false);
        startActivity(moviePageIntent);
    }

    private List<Movie> getMovies(int start, int end) throws IndexOutOfBoundsException{
        List<Movie> ret = new ArrayList<>();
        for(int i = start; i<end; i++) {
            Movie current = movies.get(i);
            if(current != null && current.getTitle() != null )
                ret.add(current);
        }
        return ret;
    }

    private void updatePageNumber(){
        TextView pageNumber = findViewById(R.id.pageNumberSA);
        pageNumber.setText(getApplicationContext().getString(R.string.page_number, currentPage));
    }

    private void setAllSuggestionsTo(int visibility){
        for (View v : suggestionButtons){
            v.setVisibility(visibility);
        }
    }

    private void setSuggestionButtons(List<Movie> moviesToBeDisplayed){
        for(View v: suggestionButtons){
            ((Button)v).setText(
                    moviesToBeDisplayed.get(
                            suggestionButtons.indexOf(v))
                            .getTitle()
            );
        }
    }

    private void updateMoviesByPageNumber(){

        int numMoviesToDisplay = suggestionButtons.size();

        List<Movie> moviesToDisplay = getMovies(
                (currentPage - 1)*numMoviesToDisplay,
                numMoviesToDisplay * (currentPage));

        setSuggestionButtons(moviesToDisplay);

    }

    public void nextOnClick(View v){
        if ((currentPage + 1) * suggestionButtons.size() > movies.size()) return;
        currentPage += 1;
        updateMoviesByPageNumber();
        updatePageNumber();
    }

    public void previousOnClick(View v){
        if (currentPage == 1) return;
        currentPage -= 1;
        updateMoviesByPageNumber();
        updatePageNumber();
    }


}

