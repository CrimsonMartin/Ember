package com.group395.ember.MovieLoader;

import android.os.AsyncTask;

import com.group395.ember.UISearch;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.group395.ember.Movie;

public class MovieLoader {

    private static int MAXNUMMOVIES = 1000;

    private static String omdbApiKey = "db5b96c2";
    private static String omdbUrl = "http://www.omdbapi.com/?";

    private static String utelliAPIKey = "6bff01b396msh0f92aae4b854e96p1277f2jsna247a9a391a8";
    private static String utelliUrl = "https://utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com/lookup?term=";

    private BufferedReader reader;


    @Deprecated
    List<Movie> loadMovies(UISearch uiSearch, Integer n) throws UnsupportedOperationException{
        //How does this know what kind of movies to search for?
        throw new UnsupportedOperationException();
    }

    /**
     * @param uiSearch the search input from the user, attempting to find a particular movie
     * @return the movie object if the loading was successful, and null otherwise
     */
    public Movie loadMovie( UISearch uiSearch ){
        return loadMoviebyTitle(uiSearch.getSearch());
    }

    /**
     *
     * @param title the string representation of the title of the movie we want to know more about
     * @return the movie object if the loading was successful, and null otherwise
     */
    public Movie loadMoviebyTitle(String title) {
        List<String> titlelist = new ArrayList<>();
        titlelist.add(title);
        List<Movie> movies = loadMoviebyTitle(titlelist);
        return movies.get(0);
    }

    /**
     *
     * @param titles a list of movie titles
     * @return the list of movies that correspond to the
     */
    public List<Movie> loadMoviebyTitle(List<String> titles) /*throws IllegalArgumentException, IOException, InterruptedException*/{
        List<URL> urls = omdbUrlFromTitle(titles);



        return null;
    }

    private List<URL> omdbUrlFromTitle (List<String>  titles){
       List<URL> urls = new ArrayList<>();
       try {
           for (String t : titles) {
               urls.add(omdbUrlFromTitle(t));
           }
       }catch (MalformedURLException e){
           System.out.println("Malformed URL: " + e.getMessage());
           return null;
       }
       return urls;
    }

    private URL omdbUrlFromTitle (String title) throws MalformedURLException {
        title = title.replaceAll(" ", "+");
        return new URL(omdbUrl +"apikey=" + omdbApiKey + "&t=" + title + "&plot=full");
    }


    private String createUtelliSearchURL(String movieTitle){
        return utelliUrl + movieTitle.replaceAll(" ", "+").toLowerCase();
    }

    /**
     *
     * @param m Movie object to load the available platforms for
     */
    void loadPlatforms(Movie m){
        try{
            HttpResponse<JsonNode> response = Unirest.get(createUtelliSearchURL(m.getTitle()))
                .header("X-RapidAPI-Key", utelliAPIKey)
                .asJson();

            loadPlatforms(m,response.getBody().toString());

        }catch (UnirestException e){
            System.out.println("loading platform failed, exception " + e.getMessage());
        }
    }

    /**
     *
     * @param m the movie to load the available platforms for
     * @param json String representation of the json response from requesting the available platforms from UTelli
     */
    void loadPlatforms(Movie m, String json){
        m.addPlatforms(json);
    }

}
