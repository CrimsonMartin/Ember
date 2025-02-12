package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MovieSearch {

    private static String tmdbApiKey = "2798eab352dd4b7d99e4a0f825802ff5";
    private static String tmdbUrl = "https://api.themoviedb.org/3/";
    private static String tmdbSearchUrl = "search/movie?api_key=";
    private static String tmdbSearchPeopleUrl = "search/person?api_key=";
    private static String tmdbMoviesByPersonUrl = "https://api.themoviedb.org/3/person/";
    private static String tmdbMovieCredits = "/movie_credits?api_key=";
    private static String tmdbSettings = "&language=en-US&include_adult=false";

    private static final String omdbApiKey = "db5b96c2";
    private static final String omdbUrl = "http://www.omdbapi.com/?";

    private static MovieLoader loader = new MovieLoader();
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static Integer MAXNUMMOVIES = 100;
    public BlockingQueue<Movie> results = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    public int totalResults = -1;
    public int pages = -1;
    public boolean searchComplete = false;
    public boolean searchSuccessful = true;
    private static BufferedReader reader = null;


    /**
     * This method is void and starts up a search by actor thread for a given search query.
     * The results can be accessed via calling .poll or .take on the public field "results"
     */
    public void searchByActor(String actor) {
        executor.submit(new SearchByActorThread(actor));
    }

    private class SearchByActorThread implements Runnable {
        private String query;

        public SearchByActorThread(String actor){
            query = actor;
        }

        @Override
        public void run() {
            PersonResults personResults = null;
            MoviesByPersonResults movies = null;
            List<Future<Movie>> loaded = new ArrayList<>();

                //Getting the list of movies to load
            try {
                Gson gson = new Gson();
                URL person = new URL(tmdbSearchPeople(query));
                HttpURLConnection con = (HttpURLConnection) person.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                personResults = gson.fromJson(reader, PersonResults.class);
                reader.close();
                if (personResults.getTotal_results() > 0) {
                    URL movieCredits = new URL(tmdbMoviesByPerson(personResults.getId()));
                    HttpURLConnection con2 = (HttpURLConnection) movieCredits.openConnection();
                    con2.setRequestMethod("GET");
                    reader = new BufferedReader(new InputStreamReader(con2.getInputStream()));
                    movies = gson.fromJson(reader, MoviesByPersonResults.class);
                    loaded.addAll(loader.loadMovies(movies.getResults()));
                    for (int i = 0; i < loaded.size(); i++) {
                        results.add(loaded.get(i).get(3, TimeUnit.SECONDS));
                    }
                } else {
                    searchSuccessful = false;
                    results.add(new Movie("No results found containing: " + query));
                    results.add(new Movie(" "));
                    totalResults = 0;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            searchComplete = true;
        }
    }

    /**
     * This method is void and starts up a search by title thread for a given search query.
     * The results can be accessed via calling .poll or .take on the public field "results"
     */
    public void searchFull(String title) {
        executor.submit(new SearchFullThread(title));
    }

    private class SearchFullThread implements Runnable {

        private String query;

        public SearchFullThread(String q){
            query = q;
        }

        @Override
        public void run(){
            OmdbSearchResults searchResults = null;
            List<Future<Movie>> loaded = new ArrayList<>();

            try {

                //TODO rewrite to a single for loop
                Gson gson = new Gson();
                URL obj = new URL(omdbSearch(query, 1));
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                searchResults = gson.fromJson(reader, OmdbSearchResults.class);
                reader.close();
            } catch (Exception e){
                e.printStackTrace();
            }
            if(searchResults.getResponse()) {
                loaded.addAll(loader.loadMovies(searchResults.getResults()));
                pages = searchResults.getTotal_pages();
                totalResults = searchResults.getNumberofResults();

                pages = pages > 10 ? 10 : pages;
                try {
                    for (int p = 2; p < pages; p++) {
                        Gson gson = new Gson();
                        URL obj = new URL(omdbSearch(query, p));
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod("GET");
                        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        searchResults = gson.fromJson(reader, OmdbSearchResults.class);
                        loaded.addAll(loader.loadMovies(searchResults.getResults()));
                    }
                } catch (Exception e) {
                    System.out.println("Title search failed: " + e.getMessage());
                    e.printStackTrace();
                }
                for (int i=0; i<loaded.size(); i++) {
                    try {
                        results.add(loaded.get(i).get(5, TimeUnit.SECONDS));
                    } catch (Exception e) {
                        System.out.println("Current Movie: "+i);
                        e.printStackTrace();
                    }
                }
            }
            else{
                searchSuccessful = false;
                results.add(new Movie("No results found containing: " +query));
                results.add(new Movie(" "));
                totalResults = 0;
            }
            searchComplete = true;
        }
    }

    /**
     * This method checks whether or not a search has been exhausted, meaning that the searching thread is done
     * and all of the results have been pulled out of the queue by the calling class
     * @return
     */
    public boolean isExhausted(){
        if(searchComplete && results.size() == 0)
            return true;
        else
            return false;
    }

    /**
     * This private class allows Gson to parse the json object returned from the omdb search method
     * to compile the list of movie results that are returned
     */
    private class OmdbSearchResults {
        Integer total_pages;
        Boolean Response;
        Integer totalResults;
        ArrayList<Movie> Search;

        public String toString() {
            return "Response: " + Response + ", Total Results: " + totalResults + ", Results: " + Search;
        }

        public Integer getTotal_pages() {
            total_pages = totalResults / 10 + 1;
            return total_pages;
        }

        public Integer getNumberofResults() {
            return totalResults;
        }

        public ArrayList<Movie> getResults() {
            return Search;
        }

        public boolean getResponse(){return Response; }
    }

    /**
     * this private class allows Gson to parse the results from Searching actors on tmdb
     * Most importantly, it returns the id of the first result, so that the movies that
     * person is credited with can be returned
     */
    private class PersonResults {
        Integer total_results;
        public class Actor {
            String name;
            Integer id;

            public String getName() {
                return name;
            }

            public Integer getId() {
                return id;
            }
        }

        ArrayList<Actor> results;

        public Integer getId() {
            return results.get(0).getId();
        }

        public Integer getTotal_results(){
            return total_results;
        }
    }

    /**
     * This private class allows Gson to parse the Movies Credits TMDB lists for a given person
     * It turns the results it gets into a list of movies
     */
    private class MoviesByPersonResults {
        ArrayList<TmdbMovie> cast;

        public ArrayList<Movie> getResults() {
            ArrayList<Movie> searchResults = new ArrayList<Movie>();
            for (TmdbMovie movie : cast) {
                searchResults.add(movie.toMovie());
            }
            return searchResults;
        }
    }

    /**
     * omdbSearch returns the HTTP Request for the omdb search API for a given title and page number
     * @param title the title of the movie to search for
     * @param page the page of the results that will be returned
     * @return a String containing the URL for the omdb search HTTP request
     */
    private static String omdbSearch(String title, Integer page) {
        title = title.replaceAll(" ", "+");
        return omdbUrl + "apikey=" + omdbApiKey + "&s=" + title + "&page=" + page;
    }

    /**
     * tmdbSearchPeople produces a url for TMDb's people search API
     * @param name the name of the actor to search for
     * @return the url to call the TMDb people search HTTP request
     */
    private static String tmdbSearchPeople(String name) {
        name = name.replaceAll(" ", "+");
        return tmdbUrl + tmdbSearchPeopleUrl + tmdbApiKey + tmdbSettings + "&query=" + name;
    }

    /**
     * tmdbMoviesByPerson produces a url for TMDb's Movie Credits API method based on an actor's id
     * @param id the TMDb id of the actor you are searching for
     * @return the url to call the TMDb movie credit method
     */
    private static String tmdbMoviesByPerson(Integer id) {
        return tmdbMoviesByPersonUrl + id + tmdbMovieCredits + tmdbApiKey + tmdbSettings;
    }

}