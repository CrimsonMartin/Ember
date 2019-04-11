package com.group395.ember;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

public class SearchResultsActivity extends AppCompatActivity {

    private static Movie[] loadedMovies = new Movie[2];
    //This field specifies how many sets of 2 Movies have been moved past by the "next" button.
    private static int pagesSkipped = 0;
    private static UISearch mySearch = new UISearch();

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
        displayAll();
    }

    /**
     * @param searchText The name of the movie being searched.
     * @param actorNotTitle True if searching by actor, false if searching by title.
     */
    protected static void search(String searchText, boolean actorNotTitle){
        UISearch.searchFromButton(searchText, actorNotTitle);
        loadedMovies = UISearch.getTwo(pagesSkipped);
        //loadedMovies[0] = moviesToLoad[0];
        //loadedMovies[1] = moviesToLoad[1];
        //loadedMovies[0] = exampleMovie;
        //loadedMovies[1] = exampleMovie;
    }

    public void searchAgainOnClick(View v){ startActivity(new Intent(SearchResultsActivity.this, SearchStartActivity.class)); }

    public void resultButtonAOnClick(View v) {
        try {
            HistoryActivity.addClick(loadedMovies[0]);
            MoviePageActivity.setCurrentMovie(loadedMovies[0]);
            MoviePageActivity.setFromHistoryActivity(false);
            Logger.saveToHistory(loadedMovies[0]);
            startActivity(new Intent(SearchResultsActivity.this, MoviePageActivity.class));
        } catch(IOException io) {
            System.out.println(io.toString());
        }
    }
    public void resultButtonBOnClick(View v){
        try {
            HistoryActivity.addClick(loadedMovies[1]);
            MoviePageActivity.setCurrentMovie(loadedMovies[1]);
            MoviePageActivity.setFromHistoryActivity(false);
            Logger.saveToHistory(loadedMovies[1]);
            startActivity(new Intent(SearchResultsActivity.this, MoviePageActivity.class));
        } catch(IOException io) {
            System.out.println(io.toString());
        }

    }

    public void filtersOnClick(View v){ startActivity(new Intent(SearchResultsActivity.this, SearchOptionsActivity.class)); }
    public static void addFilters(){ }

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
    private void displayAll(){
        display(loadedMovies[0], (TextView) findViewById(R.id.resultTitleA), (TextView) findViewById(R.id.resultActorsA), (TextView) findViewById(R.id.resultDirectorA), (TextView) findViewById(R.id.resultPlotA));
        display(loadedMovies[1], (TextView) findViewById(R.id.resultTitleB), (TextView) findViewById(R.id.resultActorsB), (TextView) findViewById(R.id.resultDirectorB), (TextView) findViewById(R.id.resultPlotB));
        TextView pageNumber = findViewById(R.id.pageNumber);
        pageNumber.setText(getApplicationContext().getString(R.string.page_number, pagesSkipped + 1));
    }

    public void nextOnClick(View v){
        //Movie[] moviesToLoad = mySearch.getMovies(2 * pagesSkipped + 2, 2 * pagesSkipped + 3);
        Movie[] moviesToLoad = new Movie[]{exampleMovie, exampleMovie};
        if(moviesToLoad[0] != null){
            pagesSkipped++;
            loadedMovies = moviesToLoad;
            displayAll();
        }
    }
    public void prevOnClick(View v){
        if(pagesSkipped > 0){
            pagesSkipped--;
            //loadedMovies = mySearch.getMovies(2 * pagesSkipped + 2, 2 * pagesSkipped + 3);
            loadedMovies = new Movie[]{exampleMovie, exampleMovie};
            displayAll();
        }
    }
    public void backOnClick(View v){ startActivity(new Intent(SearchResultsActivity.this, SearchStartActivity.class)); }

    protected static String stripBrackets(String input){ return input.substring(1, input.length() - 1); }
}
