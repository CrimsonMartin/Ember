package com.group395.ember;

import android.support.annotation.VisibleForTesting;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MovieLoader {

    private static int MAXNUMTHREADS = 4;
    private ExecutorService executor = Executors.newFixedThreadPool(MAXNUMTHREADS);

    private class MovieLoaderThread implements Callable<Movie> {

        private Movie movie;
        private static final String omdbApiKey = "db5b96c2";
        private static final String omdbUrl = "http://www.omdbapi.com/?";

        private String omdbUrlFromTitle (String title)throws MalformedURLException {
            title = title.replaceAll(" ", "+");
            return omdbUrl +"apikey=" + omdbApiKey + "&t=" + title + "&plot=full";
        }

        public MovieLoaderThread(Movie m) {
            movie = m;
        }

        @Override
        public Movie call() throws Exception{

            String movieTitle = movie.getTitle();
            String url = omdbUrlFromTitle(movieTitle);

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            return Movie.parseFromReader(reader);
        }
    }

    private class LoadPlatformsThread implements Runnable{

        private Movie movie;

        LoadPlatformsThread (Movie m){ movie = m; }

        private String utelliAPIKey = "6bff01b396msh0f92aae4b854e96p1277f2jsna247a9a391a8";
        private String utelliUrl = "https://utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com/lookup?term=";

        private String createUtelliSearchURL(String movieTitle) {
            return utelliUrl + movieTitle.replaceAll(" ", "+").toLowerCase();
        }

        @Override
        public void run() {
            try {
                HttpResponse<JsonNode> response = Unirest.get(createUtelliSearchURL(movie.getTitle()))
                        .header("X-RapidAPI-Key", utelliAPIKey)
                        .asJson();

                    movie.addPlatforms(response.getBody().toString());

            } catch (UnirestException e) {
                //System.out.println("loading platform failed, exception " + e.getMessage());
            }
        }
    }


    /**
     *
     * @param m The Movie, with title, to be loaded.
     * @return Future<Movie> the Movie complete with all information</Movie>
     */
    public Future<Movie> loadMovie(Movie m) {
        return executor.submit(new MovieLoaderThread(m));
    }

    /**
     * Load a list of movies in place
     * @param movies Replace movies in place in the list
     */
    public List<Future<Movie>> loadMovies(List<Movie> movies){
        List<Future<Movie>> ret = new ArrayList<>();
        for(Movie m : movies){
           ret.add (executor.submit(new MovieLoaderThread(m)));
        }
        return ret;
    }

    /**
     * @param title the string representation of the title of the movie we want to know more about
     * @return the movie object if the loading was successful, and null otherwise
     */
    public Future<Movie> loadMovieByTitle(String title) {
        return (executor.submit(new MovieLoaderThread(new Movie(title))));
    }

    /**
     * @param titles a list of movie titles to be loaded
     * @return the list of movies that correspond to the titles
     */
    public List<Future<Movie>> loadMoviesByTitle(List<String> titles) throws InterruptedException{
        List<Future<Movie>> ret = new ArrayList<>();
        for(String t : titles){
            ret.add (executor.submit(new MovieLoaderThread(new Movie(t))));
        }
        return ret;
    }

    /**
     * @param m Movie object to load the available platforms for
     */
    public void loadPlatforms(Movie m) {
        LoadPlatformsThread lpt = new LoadPlatformsThread(m);
        lpt.run();
    }

    /**
     * This only exists for unit testing, because Utelly costs money if we go over our number of api calls per month.
     * Don't actually use this.
     * @param m    the movie to load the available platforms for
     * @param json String representation of the json response from requesting the available platforms from UTelli
     */
    @VisibleForTesting
    void loadPlatforms(Movie m, String json) {
        m.addPlatforms(json);
    }

}
