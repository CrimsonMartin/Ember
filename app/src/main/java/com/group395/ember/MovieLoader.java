package com.group395.ember;

import android.support.annotation.VisibleForTesting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class MovieLoader {

    private static int MAXNUMTHREADS = 4;
    private ExecutorService executor = Executors.newFixedThreadPool(MAXNUMTHREADS);

    private Map<Movie, Future<Movie>> movieCache = new HashMap<>();
    private Map<Movie, Set<String>> platformCache = new HashMap<>();


    private class MovieLoaderThread implements Callable<Movie> {

        private Movie movie;
        private static final String omdbApiKey = "db5b96c2";
        private static final String omdbUrl = "http://www.omdbapi.com/?";

        private String omdbUrlFromTitle (String title) {
            title = title.replaceAll(" ", "+");
            return omdbUrl +"apikey=" + omdbApiKey + "&t=" + title + "&plot=full";
        }

        private String omdbUrlFromID (String id) {
            return omdbUrl +"apikey=" + omdbApiKey + "&i=" + id + "&plot=full";
        }

        MovieLoaderThread(Movie m) {
            movie = m;
        }

        @Override
        public Movie call() throws Exception{

            String url;
            String movieID = movie.getImdbID();
            if (movieID == null){
                url = omdbUrlFromTitle(movie.getTitle());
            }else{
                url = omdbUrlFromID(movieID);
            }

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            return Movie.parseFromReader(reader);
        }
    }

    private class LoadPlatformsThread implements Callable<Movie>{

        private Movie movie;

        LoadPlatformsThread (Movie m){ movie = m; }

        private String utelliAPIKey = "6bff01b396msh0f92aae4b854e96p1277f2jsna247a9a391a8";
        private String utelliUrl = "https://utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com/lookup?term=";

        private String createUtelliSearchURL(String movieTitle) {
            return utelliUrl + movieTitle.replaceAll(" ", "+").toLowerCase() + "&country=us";
        }

        @Override
        public Movie call() {
            try {
                String url = createUtelliSearchURL(movie.getTitle());
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-RapidAPI-Host", "utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com");
                con.setRequestProperty("X-RapidAPI-Key", utelliAPIKey);
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                movie.addPlatforms(reader);

            } catch (Exception e) {
                //System.out.println("loading platform failed, exception " + e.getMessage());
            }
            return movie;
        }
    }

    /**
     *
     * @param m The Movie, with title, to be loaded.
     * @return Future<Movie> the Movie complete with all information</Movie>
     */
    Future<Movie> loadMovie(Movie m) {

        if (movieCache.containsKey(m)){
            return movieCache.get(m);

        }else{
            Future<Movie> futureMovie = executor.submit(new MovieLoaderThread(m));
            movieCache.put(m, futureMovie);
            return futureMovie;
        }
    }

    /**
     * Load a list of movies in place
     * @param movies Replace movies in place in the list
     */
    List<Future<Movie>> loadMovies(List<Movie> movies){
        List<Future<Movie>> ret = new ArrayList<>();
        for(Movie m : movies){
           ret.add(loadMovie(m));
        }
        return ret;
    }

    /**
     * @param title the string representation of the title of the movie we want to know more about
     * @return the movie object if the loading was successful, and null otherwise
     */
    Future<Movie> loadMovieByTitle(String title) {
        return (loadMovie(new Movie(title)));
    }

    /**
     * @param titles a list of movie titles to be loaded
     * @return the list of movies that correspond to the titles
     */
    List<Future<Movie>> loadMoviesByTitle(List<String> titles){
        List<Movie> movies = new ArrayList<>();
        for(String t : titles){
            movies.add(new Movie(t));
        }
        return loadMovies(movies);
    }

    /**
     * @param m Movie object to load the available platforms for
     */
    Set<String> loadPlatforms(Movie m) {
        try{
            if (platformCache.containsKey(m)){
                return platformCache.get(m);
            } else{
                Future<Movie> futureMovie = executor.submit(new LoadPlatformsThread(m));
                platformCache.put(m, futureMovie.get().getPlatforms());
            }
        } catch(InterruptedException | ExecutionException e){
            //pass
        }
        return platformCache.get(m);
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
