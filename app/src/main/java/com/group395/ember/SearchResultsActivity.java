package com.group395.ember;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SearchResultsActivity extends AppCompatActivity {

    private static Movie[] loadedMovies = new Movie[2];
    public static Movie exampleMovie = Movie.parseFromJson("{\"Title\":\"Space Jam\",\"Year\":\"1996\",\"Rated\":\"PG\",\"Released\":\"15 Nov 1996\",\"Runtime\":\"88 min\",\"Genre\":\"Animation, Adventure, " +
            "Comedy, Family, Fantasy, Sci-Fi, Sport\",\"Director\":\"Joe Pytka\",\"Writer\":\"Leo Benvenuti, Steve Rudnick, Timothy Harris, Herschel Weingrod\",\"Actors\":\"Michael Jordan, Wayne Knight, " +
            "Theresa Randle, Manner Washington\",\"Plot\":\"In a desperate attempt to win a basketball match and earn their freedom, the Looney Tunes seek the aid of retired basketball champion, Michael Jordan. " +
            "\",\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"5 wins & 7 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMDgyZTI2YmYtZmI4ZC00MzE0LWIxZWYtMWRlZWYxNjliNTJjXkEyXkFqcG" +
            "deQXVyNjY5NDU4NzI@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"6.4/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"38%\"},{\"Source\":\"Metacritic\",\"Value\":" +
            "\"59/100\"}],\"Metascore\":\"59\",\"imdbRating\":\"6.4\",\"imdbVotes\":\"138,962\",\"imdbID\":\"tt0117705\",\"Type\":\"movie\",\"DVD\":\"27 Aug 1997\",\"BoxOffice\":\"N/A\",\"Production\":\"Warner " +
            "Home Video\",\"Website\":\"N/A\",\"Response\":\"True\"}"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        //TODO: Test boundary cases (large + small movie data sets) once UISearch is up and running
        display(loadedMovies[0], (TextView) findViewById(R.id.resultTitleA), (TextView) findViewById(R.id.resultActorsA), (TextView) findViewById(R.id.resultDirectorA), (TextView) findViewById(R.id.resultPlotA));
        display(loadedMovies[1], (TextView) findViewById(R.id.resultTitleB), (TextView) findViewById(R.id.resultActorsB), (TextView) findViewById(R.id.resultDirectorB), (TextView) findViewById(R.id.resultPlotB));
    }

    /**
     * @param searchText The name of the movie being searched.
     * @param myFilters The filters applied to the search.
     */
    protected static void search(String searchText, Filter[] myFilters){
        //TODO: Retrieve a pair of movies from the search property
        //UISearch crashes upon initialization
        UISearch mySearch = new UISearch();
        //for(int i = 0; i < 3; i++){
        //    if(myFilters[i] != null){
        //        mySearch.addFilter(myFilters[i]);
        //    }
        //}
        loadedMovies[0] = exampleMovie;
        loadedMovies[1] = exampleMovie;
    }

    public void searchAgainOnClick(View v){ startActivity(new Intent(SearchResultsActivity.this, SearchOptionsActivity.class)); }

    public void resultButtonAOnClick(View v){
        //TODO: Design and implement movie display page
        HistoryActivity.addClick(loadedMovies[0]);
    }
    public void resultButtonBOnClick(View v){
        HistoryActivity.addClick(loadedMovies[1]);
    }

    /**
     * A helper method that affixes a single movie's JSON data to pre-made display elements, which are passed as parameters.
     * @param title Set at the top of the button, will contain the title and date in a set format.
     */
    private void display(Movie inputMovie, TextView title, TextView actors, TextView director, TextView plot){
        title.setText(getApplicationContext().getString(R.string.title_with_year, inputMovie.getTitle(), inputMovie.getYear().toString()));
        actors.setText(getApplicationContext().getString(R.string.starring, stripBrackets(inputMovie.getActors().toString())));
        director.setText(getApplicationContext().getString(R.string.directed_by, inputMovie.getDirector()));
        plot.setText(inputMovie.getPlot());
        title.bringToFront();
        actors.bringToFront();
        director.bringToFront();
        plot.bringToFront();
    }

    private String stripBrackets(String input){ return input.substring(1, input.length() - 1); }
    private String crop(String input, int numLetters) {
        if (input.length() > numLetters) {
            return input.substring(0, numLetters - 4) + "...";
        }
        return input;
    }
}
