package com.group395.ember;

import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MovieLoader {

    private static int MAXNUMMOVIES = 1000;
    private static int MAXNUMTHREADS = 5;
    private BlockingQueue<String> movietitles = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    public BlockingQueue<Movie> loadedmovies = new ArrayBlockingQueue<>(MAXNUMMOVIES);
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
                String movietitle ;
                String response = null;

                try {
                    movietitle = movietitles.take();
                    //System.out.println("Loading title: " + movietitle);
                    String url = omdbUrlFromTitle(movietitle);

                    response = Unirest.get(url).asJson().getBody().toString();

                } catch (UnirestException | MalformedURLException | InterruptedException e) {
                    //System.out.println("loading failed " + e.getMessage());
                }
                try {
                    if (response != null) {
                        loadedmovies.put(Movie.parseFromJson(response));
                    }
                } catch (InterruptedException e) {
                    return;
                }
                movietitle = null;
            }
            //System.out.println("Queue is empty, thread terminating");
        }

    }

    private class LoadPlatformsTask extends AsyncTask<Movie, Void, Void> {

        private String utelliAPIKey = "6bff01b396msh0f92aae4b854e96p1277f2jsna247a9a391a8";
        private String utelliUrl = "https://utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com/lookup?term=";

        private String createUtelliSearchURL(String movieTitle) {
            return utelliUrl + movieTitle.replaceAll(" ", "+").toLowerCase();
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            try {
                HttpResponse<JsonNode> response = Unirest.get(createUtelliSearchURL(movies[0].getTitle()))
                        .header("X-RapidAPI-Key", utelliAPIKey)
                        .asJson();

                if (!isCancelled()){
                    movies[0].addPlatforms(response.getBody().toString());
                }

            } catch (UnirestException e) {
                //System.out.println("loading platform failed, exception " + e.getMessage());
            }
            return null;
        }
    }

    /**
     * @param title the string representation of the title of the movie we want to know more about
     * @return the movie object if the loading was successful, and null otherwise
     */
    public void loadMoviebyTitle(String title) {
        movietitles.add(title);
        //System.out.println("Starting new thread");
        //System.out.println("Titlelist has" + movietitles.peek());
        executor.submit(new MovieLoaderThread());
    }

    /**
     * @param titles a list of movie titles to be loaded
     * @return the list of movies that correspond to the titles
     */
    public void loadMoviebyTitle(List<String> titles) {
        //System.out.println("attempting to put all titles");
        if (!attemptPutAll(titles)) return;
        //System.out.println("Put "  + movietitles.size() + " elements");

        //System.out.println("Active Count is "+ pool.getActiveCount());
        //System.out.println("Max Size  is " + pool.getMaximumPoolSize());
        while (pool.getActiveCount() < pool.getMaximumPoolSize()) {
            //System.out.println("Pool Submitting Thread");
            executor.submit(new MovieLoaderThread());
            //System.out.println("Pool size is now "+ pool.getActiveCount());
        }
    }

    private boolean attemptPutAll(List<String> titles){
        try{
            for (String str : titles){
                movietitles.put(str);
            }
        }catch(InterruptedException e){
            return false;
        }
        return true;
    }

    /**
     * @param m Movie object to load the available platforms for
     */
    public void loadPlatforms(Movie m) {
        LoadPlatformsTask t = new LoadPlatformsTask();
        t.execute(m);
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
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch(InterruptedException e){
            executor.shutdownNow();
        }
        return executor.isShutdown();
    }
}
