package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieSuggestions {

    private static String tmdbApiKey = "2798eab352dd4b7d99e4a0f825802ff5";
    private static String tmdbUrl = "https://api.themoviedb.org/3/movie/";
    private static String tmdbSuggestionUrl = "recommendations?api_key=";
    private static String tmdbSettings = "&language=en-US&include_adult=false&page=1";

    private static BufferedReader reader = null;

    // Returns a list of movies
    public static ArrayList<Movie> getSuggestions(Movie movie){
        try{
            Integer id = movie.getTmdbID();
            Gson gson = new Gson();
            URL obj = new URL(tmdbSuggestions(id));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            SuggestionResults results = gson.fromJson(reader, SuggestionResults.class);
            return results.getResults();
        }
        catch (MalformedURLException e){
            System.out.println("Title search failed: " + e.getMessage());
            e.printStackTrace();
            close();
            return null;
        } catch (IOException e) {
            System.out.println("INVALID URL FORMAT");
            e.printStackTrace();
            close();
            return null;
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
