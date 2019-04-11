package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MovieSuggestions {

    private static String tmdbApiKey = "2798eab352dd4b7d99e4a0f825802ff5";
    private static String tmdbUrl = "https://api.themoviedb.org/3/movie/";
    private static String tmdbSuggestionUrl = "recommendations?api_key=";
    private static String tmdbSettings = "&language=en-US&include_adult=false&page=1";

    private static int MAXNUMMOVIES = 1000;
    private static int MAXNUMTHREADS = 8;
    private static BlockingQueue<String> movietitles = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    private static ExecutorService executor = Executors.newFixedThreadPool(MAXNUMTHREADS);
    private static ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
    private static boolean running = false;
    private static String exception = "";

    private static BufferedReader reader = null;
    public static Movie currentMovie;
    public static BlockingQueue<Movie> loadedSuggestions = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    public int totalResults = 0;
    public int currentPage = 0;


    // Returns a list of movies
    public static ArrayList<Movie> getSuggestions(Movie movie){
        ArrayList<Movie> suggestions = new ArrayList<Movie>();
        currentMovie = movie;
        try {
            //System.out.println("Loader thread starting");
            String response = null;
            executor.submit(new SuggestionsThread());
            do {
                suggestions.add(loadedSuggestions.poll(5, TimeUnit.SECONDS));
            }while(!loadedSuggestions.isEmpty());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return suggestions;
    }

    private static class SuggestionsThread implements Runnable {

        @Override
        public void run(){
            running = true;
            try {
                Integer id = currentMovie.getTmdbID();
                Gson gson = new Gson();
                URL obj = new URL(tmdbSuggestions(id));
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                SuggestionResults results = gson.fromJson(reader, SuggestionResults.class);
                loadedSuggestions.addAll(results.getResults());
            }
            catch (MalformedURLException e){
                System.out.println("Title search failed: " + e.getMessage());
                e.printStackTrace();
                exception = e.toString();
                close();
            } catch (IOException e) {
                System.out.println("INVALID URL FORMAT");
                e.printStackTrace();
                exception = e.toString();
                close();
            }
            finally {
                running = false;
            }
        }
    }

    //This is the class Gson parses to return the search results
    public class SuggestionResults{
        ArrayList<TmdbMovie> results;

        public String toString(){
            return "Results: " + results.toString();
        }

        public ArrayList<Movie> getResults(){
            ArrayList<Movie> suggestionResults = new ArrayList<Movie>();
            for(TmdbMovie movie : results){
                suggestionResults.add(movie.toMovie());
            }
            return suggestionResults;
        }
    }

    public static String tmdbSuggestions(Integer id){
        return tmdbUrl + id +"/" + tmdbSuggestionUrl + tmdbApiKey + tmdbSettings;
    }

    static boolean close(){
        try{
            reader.close();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
}
