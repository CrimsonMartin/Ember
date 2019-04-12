package com.group395.ember;

import android.support.annotation.VisibleForTesting;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MovieLoader {

    public BlockingQueue<Movie> LoadedMovies = new ArrayBlockingQueue<>(MAXNUMMOVIES);

    private static int MAXNUMMOVIES = 1000;
    private static int MAXNUMTHREADS = 8;
    private BlockingQueue<String> movietitles = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    private ExecutorService executor = Executors.newFixedThreadPool(MAXNUMTHREADS);
    private ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
    private static boolean run = true;

    private class MovieLoaderThread implements Runnable {

        private static final String omdbApiKey = "db5b96c2";
        private static final String omdbUrl = "http://www.omdbapi.com/?";

        private String omdbUrlFromTitle (String title)throws MalformedURLException {
            title = title.replaceAll(" ", "+");
            return omdbUrl +"apikey=" + omdbApiKey + "&t=" + title + "&plot=full";
        }

        @Override
        public void run() {
            while (MovieLoader.run && movietitles.peek() != null) {
                //System.out.println("Loader thread starting");
                String movieTitle;
                String response;

                try {
                    movieTitle = movietitles.take();
                    String url = omdbUrlFromTitle(movieTitle);

                    response = Unirest.get(url).asJson().getBody().toString();

                    if (response != null) {
                        LoadedMovies.put(Movie.parseFromJson(response));
                    }

                } catch (UnirestException | MalformedURLException | InterruptedException e) {
                    System.out.println("loading failed " + e.getMessage());
                    //pass - grab the next movie and repeat
                    try{
                        LoadedMovies.put(new Movie());
                    } catch (InterruptedException e2){
                        //pass
                    }
                }

            }
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

    private class MovieLoaderCallable implements Callable<Movie> {

        Movie movie;
        Integer i;

        public MovieLoaderCallable(/*List<Future<Movie>> tobereturned, List<Movie> m*/ Movie m, Integer i){
            movie = m;
            i = i;
            //TODO put the movie into a list at the index i;
        }

        @Override
        public Movie call() throws Exception{
                //start the load
            loadMoviebyTitle(movie.getTitle());
            return LoadedMovies.take();
        }
    }

    public Future<Movie> loadMovie(Movie m) {
        return executor.submit(new MovieLoaderCallable(m, 0));
    }

    public List<Future<Movie>> loadMovies(List<Movie> ms){
        List<Future<Movie>> ret = new ArrayList<>();
        for(Movie m : ms){
            System.out.println("Adding "+ m.getTitle() + " to loading queue");
           ret.add (executor.submit(new MovieLoaderCallable(m, ms.indexOf(m))));
        }
        return ret;
    }

    /**
     * Load a list of movies in place
     * @param movies Replace movies in place in the list
     */ /*
    public List<Future<Movie>> loadMovies(List<Movie> movies){
        List<Future<Movie>> returned = new ArrayList<Future<Movie>>();

        MovieAssembler ma = new MovieAssembler(returned, movies);
        ma.run();
    }*/

    /**
     * @param title the string representation of the title of the movie we want to know more about
     * @return the movie object if the loading was successful, and null otherwise
     */
    public void loadMoviebyTitle(String title) {
        movietitles.add(title);
        executor.submit(new MovieLoaderThread());
    }

    /**
     * @param titles a list of movie titles to be loaded
     * @return the list of movies that correspond to the titles
     */
    public void loadMoviebyTitle(List<String> titles) throws InterruptedException{
        attemptPutAll(titles);
        while (pool.getActiveCount() < pool.getMaximumPoolSize()) {
            executor.submit(new MovieLoaderThread());
        }
    }

    private void attemptPutAll(List<String> titles) throws InterruptedException{
        for (String str : titles){
            movietitles.put(str);
        }
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

    /**
     *
     * @return if the close process was completed successfully or not
     */
    boolean close() {
        MovieLoader.run = false;
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch(InterruptedException e){
            executor.shutdownNow();
        }
        return executor.isShutdown();
    }

}
