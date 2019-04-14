package com.group395.ember;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class SearchResultsActivity extends AppCompatActivity {

    private Movie[] loadedMovies = new Movie[2];
    //This field specifies how many sets of 2 Movies have been moved past by the "next" button.
    private static int pagesSkipped = 0;
    public static UISearch uiSearch = new UISearch();


    public static Movie exampleMovie = Movie.parseFromJson("{\"Title\":\"Space Jam\",\"Year\":\"1996\",\"Rated\":\"PG\",\"Released\":\"15 Nov 1996\",\"Runtime\":\"88 min\",\"Genre\":\"Animation, Adventure, " +
            "Comedy, Family, Fantasy, Sci-Fi, Sport\",\"Director\":\"Joe Pytka\",\"Writer\":\"Leo Benvenuti, Steve Rudnick, Timothy Harris, Herschel Weingrod\",\"Actors\":\"Michael Jordan, Wayne Knight, " +
            "Theresa Randle, Manner Washington\",\"Plot\":\"In a desperate attempt to win a basketball match and earn their freedom, the Looney Tunes seek the aid of retired basketball champion, Michael Jordan. " +
            "\",\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"5 wins & 7 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMDgyZTI2YmYtZmI4ZC00MzE0LWIxZWYtMWRlZWYxNjliNTJjXkEyXkFqcG" +
            "deQXVyNjY5NDU4NzI@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"6.4/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"38%\"},{\"Source\":\"Metacritic\",\"Value\":" +
            "\"59/100\"}],\"Metascore\":\"59\",\"imdbRating\":\"6.4\",\"imdbVotes\":\"138,962\",\"imdbID\":\"tt0117705\",\"Type\":\"movie\",\"DVD\":\"27 Aug 1997\",\"BoxOffice\":\"N/A\",\"Production\":\"Warner " +
            "Home Video\",\"Website\":\"N/A\",\"Response\":\"True\"}"
    );

    private class LoadMoviesTask extends AsyncTask<Void, Void, Void> {

        private Context context;
        private boolean actorNotTitle = getIntent().getBooleanExtra("actorNotTitle", false);
        private String searchInput = getIntent().getStringExtra("searchInput");
        private boolean newSearch = getIntent().getBooleanExtra("newSearch", false);

        public LoadMoviesTask (Context ctx){
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loadingProgressBar1).setVisibility(View.VISIBLE);
            findViewById(R.id.loadingProgressBar1).bringToFront();
            findViewById(R.id.loadingProgressBar2).setVisibility(View.VISIBLE);
            findViewById(R.id.loadingProgressBar2).bringToFront();
            findViewById(R.id.resultButtonA).setVisibility(View.INVISIBLE);
            findViewById(R.id.resultButtonB).setVisibility(View.INVISIBLE);
        }

        @Override //Set the visibility back from the loading bars
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            findViewById(R.id.loadingProgressBar1).setVisibility(View.INVISIBLE);
            findViewById(R.id.loadingProgressBar2).setVisibility(View.INVISIBLE);
            findViewById(R.id.resultButtonA).setVisibility(View.VISIBLE);
            findViewById(R.id.resultButtonA).bringToFront();
            findViewById(R.id.resultButtonB).setVisibility(View.VISIBLE);
            findViewById(R.id.resultButtonB).bringToFront();
            displayAll();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(newSearch) {
                uiSearch = new UISearch();
                uiSearch.setSearch(searchInput);
            }

            uiSearch.searchFromButton(searchInput, actorNotTitle);
            loadedMovies = uiSearch.getTwo(pagesSkipped);

            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        //TODO: Test boundary cases (large + small movie data sets) once UISearch is up and running
        new LoadMoviesTask(this).execute();
    }

    public void searchAgainOnClick(View v){
        uiSearch = new UISearch();
        startActivity(new Intent(SearchResultsActivity.this, SearchStartActivity.class));
    }

    public void resultButtonAOnClick(View v) throws NullPointerException, IOException{
        HistoryActivity.addClick(loadedMovies[0]);
        MoviePageActivity.setCurrentMovie(loadedMovies[0]);
        MoviePageActivity.setFromHistoryActivity(false);
        Logger.saveToHistory(loadedMovies[0]);
        startActivity(new Intent(SearchResultsActivity.this, MoviePageActivity.class));
    }
    public void resultButtonBOnClick(View v) throws NullPointerException, IOException{
        HistoryActivity.addClick(loadedMovies[1]);
        MoviePageActivity.setCurrentMovie(loadedMovies[1]);
        MoviePageActivity.setFromHistoryActivity(false);
        Logger.saveToHistory(loadedMovies[1]);
        startActivity(new Intent(SearchResultsActivity.this, MoviePageActivity.class));
    }

    public void filtersOnClick(View v){ startActivity(new Intent(SearchResultsActivity.this, SearchOptionsActivity.class)); }
    public static void addFilters(){ }

    private void display(Movie inputMovie, TextView title, TextView actors, TextView director, TextView plot){
        if(inputMovie != null) {
            if(inputMovie.getYear() > 0) { title.setText(getApplicationContext().getString(R.string.title_with_year, inputMovie.getTitle(), inputMovie.getYear().toString())); }
            else{ title.setText(inputMovie.getTitle()); }
            if(inputMovie.getActors() != null){ actors.setText(getApplicationContext().getString(R.string.starring, stripBrackets(inputMovie.getActors().toString()))); }
            else{ actors.setText("No actors found"); }
            if(inputMovie.getDirector() != null){ director.setText(getApplicationContext().getString(R.string.directed_by, inputMovie.getDirector())); }
            else{ director.setText("No directors found"); }
            if(inputMovie.getPlot() != null){ plot.setText(inputMovie.getPlot()); }
            else{ plot.setText("No plot found"); }
            title.bringToFront();
            actors.bringToFront();
            director.bringToFront();
            plot.bringToFront();
        }
    }
    private void displayAll(){
        display(loadedMovies[0], (TextView) findViewById(R.id.resultTitleA), (TextView) findViewById(R.id.resultActorsA), (TextView) findViewById(R.id.resultDirectorA), (TextView) findViewById(R.id.resultPlotA));
        display(loadedMovies[1], (TextView) findViewById(R.id.resultTitleB), (TextView) findViewById(R.id.resultActorsB), (TextView) findViewById(R.id.resultDirectorB), (TextView) findViewById(R.id.resultPlotB));
        TextView pageNumber = findViewById(R.id.pageNumber);
        pageNumber.setText(getApplicationContext().getString(R.string.page_number, pagesSkipped + 1));
    }

    public void nextOnClick(View v){
        Movie[] moviesToLoad = uiSearch.getTwo(pagesSkipped + 1);
        if(moviesToLoad[0] != null){
            pagesSkipped++;
            loadedMovies = moviesToLoad;
            displayAll();
        }
    }
    public void prevOnClick(View v){
        if(pagesSkipped > 0){
            pagesSkipped--;
            loadedMovies = uiSearch.getTwo(pagesSkipped);
            displayAll();
        }
    }
    public void backOnClick(View v){
        uiSearch = new UISearch();
        startActivity(new Intent(SearchResultsActivity.this, SearchStartActivity.class));
    }

    protected static String stripBrackets(String input){ return input.substring(1, input.length() - 1); }
}
