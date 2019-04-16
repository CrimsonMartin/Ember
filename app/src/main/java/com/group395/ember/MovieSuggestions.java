package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class MovieSuggestions {

    private static String tmdbApiKey = "2798eab352dd4b7d99e4a0f825802ff5";
    private static String tmdbUrl = "https://api.themoviedb.org/3/";
    private static String tmdbSearchUrl = "search/movie?api_key=";
    private static String tmdbSuggestionUrl = "recommendations?api_key=";
    private static String tmdbExternalIdsUrl = "external_ids?api_key=";
    private static String tmdbSettings = "&language=en-US&include_adult=false&page=1";

    private MovieLoader loader = new MovieLoader();
    private BufferedReader reader = null;

    /**
     * setSuggestions returns a list of Future Movies that get filled as the fields of the suggestions do
     * @param title the title of the movie we are trying to get suggestions for
     * @return Future movies, which get filled as the movies load
     */
    List<Future<Movie>> setSuggestions(String title){
        List<Future<Movie>> loaded = new ArrayList<>();
        try {
            Integer id = getTmdbId(title);
            Gson gson = new Gson();
            URL obj = new URL(tmdbSuggestions(id));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            SuggestionResults newResults = gson.fromJson(reader, SuggestionResults.class);
            loaded.addAll(loader.loadMovies(newResults.getResults()));
        }
        catch (MalformedURLException e){
            System.out.println("Title search failed: " + e.getMessage());
            e.printStackTrace();
            close();
        } catch (IOException e) {
            System.out.println("INVALID URL FORMAT");
            e.printStackTrace();
            close();
        }
        return loaded;
    }

    /**
     * TmdbSearchResults allows Gson to parse the Json object returned by Tmdb's search method
     * This allows us to get the tmdb id of the movie we're trying to get suggestions for
     */
    private class TmdbSearchResults {
        Integer page;
        Integer total_results;
        Integer total_pages;
        ArrayList<TmdbMovie> results;

        public String toString() {
            return "Page: " + page + "Total Results: " + total_results + ", Results: " + results.get(0).toString();
        }

        public Integer getTotal_pages() {
            return total_pages;
        }

        public Integer getNumberofResults() {
            return total_results;
        }

        public ArrayList<Movie> getResults() {
            ArrayList<Movie> searchResults = new ArrayList<Movie>();
            for (TmdbMovie movie : results) {
                searchResults.add(movie.toMovie());
            }
            return searchResults;
        }
    }

    /**
     * SuggestionResults allows Gson to parse the Json object returned by TMDb's movie
     * suggestions function into movies that can get loaded by the movie loader
     */
    private class SuggestionResults{
        ArrayList<TmdbMovie> results;

        public String toString(){
            return "Results: " + results.toString();
        }

        public ArrayList<Movie> getResults(){
            ArrayList<Movie> suggestionResults = new ArrayList<Movie>();
            for(TmdbMovie movie : results){
                Movie current = movie.toMovie();
                String imdbId = getImdbIdFromTmdb(movie.getId());
                current.setImdbID(imdbId);
                suggestionResults.add(current);
            }
            return suggestionResults;
        }
    }

    /**
     * This allows us to get the IMDb Id of a given movie by allowing us to parse the
     * Json object returned by the TMDb external id API method
     */
    private class ExternalIdResults{
        String imdb_id;

        public String getImdb_id(){
            return imdb_id;
        }
    }

    /**
     * getTmdbId makes a call to get the TMDb id of a movie based on its title, so that we can
     * call the TMDb getSuggestions API method
     * @param title the title of the movie we want the id of
     * @return an integer representing the TMDb id
     */
    private Integer getTmdbId(String title){
        try {
            Gson gson = new Gson();
            URL obj = new URL(tmdbSearch(title, 1));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            TmdbSearchResults newResults = gson.fromJson(reader, TmdbSearchResults.class);
            return newResults.getResults().get(0).getTmdbID();
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * tmdbSearch takes a title and a page number and creates a url to be used to call the TMDb search API
     * @param title the title of the movie we would like to search for
     * @param page the page number of the
     * @return returns a url used to call the TMDb search API
     */
    private String tmdbSearch(String title, Integer page) {
        title = title.replaceAll(" ", "+");
        if (title.length() > 0)
            return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings + "&page=" + page + "&query=" + title;
        else
            return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings + "&page=" + page;
    }

    /**
     * tmdbSuggestions takes a title and a page number and creates a url to be used to call the TMDb search API
     * @param id the tmdb id of the movie we would like to search for
     * @return returns a url used to call the TMDb suggestion API method
     */
    private String tmdbSuggestions(Integer id){
        return tmdbUrl + "movie/"+ id +"/" + tmdbSuggestionUrl + tmdbApiKey + tmdbSettings;
    }

    /**
     * getImdbIdFromTmdb takes a tmdb id and makes the necessary calls to get a movies IMDb id
     * @param tmdbId the tmdb id of a given movie
     * @return returns a string representing the IMDb id
     */
    private static String getImdbIdFromTmdb(Integer tmdbId){
        try {
            Gson gson = new Gson();
            URL obj = new URL(tmdbUrl + "movie/" + tmdbId + "/" + tmdbExternalIdsUrl + tmdbApiKey + tmdbSettings);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            ExternalIdResults newResults = gson.fromJson(reader, ExternalIdResults.class);
            return newResults.getImdb_id();
        }catch (Exception e){
            e.printStackTrace();
            return ""+0;
        }

    }

    private

    boolean close(){
        try{
            reader.close();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
}
