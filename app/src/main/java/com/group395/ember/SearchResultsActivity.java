package com.group395.ember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class SearchResultsActivity extends AppCompatActivity {

    private static Movie[] loadedMovies = new Movie[2];
    //This field specifies how many sets of 2 Movies have been moved past by the "next" button.
    private static int pagesSkipped = 0;
    private static UISearch mySearch = new UISearch();
    //private Logger logger = new Logger(getApplicationContext());

    public static Movie exampleMovie = Movie.parseFromJson("{\"Title\":\"Space Jam\",\"Year\":\"1996\",\"Rated\":\"PG\",\"Released\":\"15 Nov 1996\",\"Runtime\":\"88 min\",\"Genre\":\"Animation, Adventure, " +
            "Comedy, Family, Fantasy, Sci-Fi, Sport\",\"Director\":\"Joe Pytka\",\"Writer\":\"Leo Benvenuti, Steve Rudnick, Timothy Harris, Herschel Weingrod\",\"Actors\":\"Michael Jordan, Wayne Knight, " +
            "Theresa Randle, Manner Washington\",\"Plot\":\"In a desperate attempt to win a basketball match and earn their freedom, the Looney Tunes seek the aid of retired basketball champion, Michael Jordan. " +
            "\",\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"5 wins & 7 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMDgyZTI2YmYtZmI4ZC00MzE0LWIxZWYtMWRlZWYxNjliNTJjXkEyXkFqcG" +
            "deQXVyNjY5NDU4NzI@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"6.4/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"38%\"},{\"Source\":\"Metacritic\",\"Value\":" +
            "\"59/100\"}],\"Metascore\":\"59\",\"imdbRating\":\"6.4\",\"imdbVotes\":\"138,962\",\"imdbID\":\"tt0117705\",\"Type\":\"movie\",\"DVD\":\"27 Aug 1997\",\"BoxOffice\":\"N/A\",\"Production\":\"Warner " +
            "Home Video\",\"Website\":\"N/A\",\"Response\":\"True\"}"
    );
    public static Movie exampleMovie1 = Movie.parseFromJson("{\"Title\":\"Star Wars: Episode IV - A New Hope\",\"Year\":\"1977\",\"Rated\":\"PG\",\"Released\":\"25 May 1977\",\"Runtime\":\"121 min\",\"Genre\":\"" +
            "Action, Adventure, Fantasy, Sci-Fi\",\"Director\":\"George Lucas\",\"Writer\":\"George Lucas\",\"Actors\":\"Mark Hamill, Harrison Ford, Carrie Fisher, Peter Cushing\",\"Plot\":\"The Imperial Forces," +
            " under orders from cruel Darth Vader, hold Princess Leia hostage in their efforts to quell the rebellion against the Galactic Empire. Luke Skywalker and Han Solo, captain of the Millennium Falcon, work " +
            "together with the companionable droid duo R2-D2 and C-3PO to rescue the beautiful princess, help the Rebel Alliance and restore freedom and justice to the Galaxy.\",\"Language\":\"English\",\"Country\":\"USA\"" +
            ",\"Awards\":\"Won 6 Oscars. Another 50 wins & 28 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BNzVlY2MwMjktM2E4OS00Y2Y3LWE3ZjctYzhkZGM3YzA1ZWM2XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg\\" +
            "https://m.media-amazon.com/images/M/MV5BNzVlY2MwMjktM2E4OS00Y2Y3LWE3ZjctYzhkZGM3YzA1ZWM2XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"8.6/10\"},{\"So" +
            "urce\":\"Rotten Tomatoes\",\"Value\":\"93%\"},{\"Source\":\"Metacritic\",\"Value\":\"90/100\"}],\"Metascore\":\"90\",\"imdbRating\":\"8.6\",\"imdbVotes\":\"1,109,357\",\"imdbID\":\"tt0076759\",\"Type\":\"m" +
            "ovie\",\"DVD\":\"21 Sep 2004\",\"BoxOffice\":\"N/A\",\"Production\":\"20th Century Fox\",\"Website\":\"http://www.starwars.com/episode-iv/\",\"Response\":\"True\"}"
    );

    public static Movie exampleMovie2 = null;
    public static Movie exampleMovie3 = null;
    public static Movie exampleMovie4 = null;
    public static Movie[] examples = {exampleMovie, exampleMovie1, exampleMovie2, exampleMovie3, exampleMovie4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        if(!HistoryActivity.searchWorks){
            loadedMovies[0] = exampleMovie;
            loadedMovies[1] = exampleMovie;
        }
        //TODO: Test boundary cases (large + small movie data sets) once UISearch is up and running
        displayAll();
    }

    /**
     * @param searchText The name of the movie being searched.
     * @param actorNotTitle True if searching by actor, false if searching by title.
     */
    protected static void search(String searchText, boolean actorNotTitle){
        if(HistoryActivity.searchWorks) {
            System.out.println("Running search(" + searchText + ", " + actorNotTitle + ")");
            UISearch.searchFromButton(searchText, actorNotTitle);
            loadedMovies = UISearch.getTwo(pagesSkipped);
            System.out.println("Finished search(" + searchText + ", " + actorNotTitle + ")");
        }else{
            loadedMovies[0] = examples[0];
            loadedMovies[1] = examples[1];
        }
    }

    public void searchAgainOnClick(View v){ startActivity(new Intent(SearchResultsActivity.this, SearchStartActivity.class)); }

    public void resultButtonAOnClick(View v) {
        try {
            if(loadedMovies[0] != null) {
                HistoryActivity.addClick(loadedMovies[0]);
                MoviePageActivity.setCurrentMovie(loadedMovies[0]);
                MoviePageActivity.setFromHistoryActivity(false);
                Logger.saveToHistory(loadedMovies[0]);
                startActivity(new Intent(SearchResultsActivity.this, MoviePageActivity.class));
            }
        } catch(IOException io) {
            System.out.println(io.toString());
        }
    }

    public void resultButtonBOnClick(View v){
        try {
            if(loadedMovies[1] != null) {
                HistoryActivity.addClick(loadedMovies[1]);
                MoviePageActivity.setCurrentMovie(loadedMovies[1]);
                MoviePageActivity.setFromHistoryActivity(false);
                Logger.saveToHistory(loadedMovies[1]);
                startActivity(new Intent(SearchResultsActivity.this, MoviePageActivity.class));
            }
        } catch(IOException io) {
            System.out.println(io.toString());
        }

    }

    public void filtersOnClick(View v){ startActivity(new Intent(SearchResultsActivity.this, SearchOptionsActivity.class)); }
    public static void addFilters(){ }

    private void display(Movie inputMovie, TextView title, TextView actors, TextView director, TextView plot){
        if(inputMovie != null) {
            title.setText(getApplicationContext().getString(R.string.title_with_year, inputMovie.getTitle(), inputMovie.getYear().toString()));
            actors.setText(getApplicationContext().getString(R.string.starring, stripBrackets(inputMovie.getActors().toString())));
            director.setText(getApplicationContext().getString(R.string.directed_by, inputMovie.getDirector()));
            plot.setText(inputMovie.getPlot());
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
        if(HistoryActivity.searchWorks) {
            Movie[] moviesToLoad = UISearch.getTwo(pagesSkipped + 1);
            if (moviesToLoad[0] != null) {
                pagesSkipped++;
                loadedMovies = moviesToLoad;
                displayAll();
            }
        }else{
            pagesSkipped++;
            loadedMovies[0] = examples [(2 * pagesSkipped) % 5];
            loadedMovies[1] = examples [(2 * pagesSkipped + 1) % 5];
            displayAll();
        }
    }
    public void prevOnClick(View v){
        if(pagesSkipped > 0){
            if(HistoryActivity.searchWorks) {
                pagesSkipped--;
                loadedMovies = UISearch.getTwo(pagesSkipped);
                displayAll();
            }else{
                pagesSkipped--;
                loadedMovies[0] = examples [(2 * pagesSkipped) % 5];
                loadedMovies[1] = examples [(2 * pagesSkipped + 1) % 5];
                displayAll();
            }
        }
    }
    public void backOnClick(View v){ startActivity(new Intent(SearchResultsActivity.this, SearchStartActivity.class)); }

    protected static String stripBrackets(String input){ return input.substring(1, input.length() - 1); }
}
