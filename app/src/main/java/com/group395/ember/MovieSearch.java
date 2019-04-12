package com.group395.ember;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
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

    private static int MAXNUMMOVIES = 1000;
    private static int MAXNUMTHREADS = 8;
    private static ExecutorService executor = Executors.newFixedThreadPool(MAXNUMTHREADS);
    private ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
    private static boolean running = false;

    public static String query = "";
    public static ArrayList<Movie> results;
    public static BlockingQueue<Movie> loadedResults = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    public static int totalResults = 0;
    public static int pages = 0;
    public static int currentPage = 0;

    private static SearchFirstPageThread firstPage = new SearchFirstPageThread();


    private static BufferedReader reader = null;

    // Returns a list of movies
    public static ArrayList<Movie> searchFirstPage(String title) {
        results = new ArrayList<Movie>();
        try {
            //System.out.println("Loader thread starting");
            String response = null;
            query = title;
            executor.submit(new SearchFirstPageThread());
            do {
                results.add(loadedResults.poll(5, TimeUnit.SECONDS));
            }while(!loadedResults.isEmpty());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private static class SearchFirstPageThread implements Runnable {

        @Override
        public void run() {
            try {
                Gson gson = new Gson();
                URL obj = new URL(omdbSearch(query, 1));
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                OmdbSearchResults results = gson.fromJson(reader, OmdbSearchResults.class);
                loadedResults.addAll(results.getResults());
            } catch (MalformedURLException e) {
                System.out.println("Title search failed: " + e.getMessage());
                e.printStackTrace();
                try {
                    reader.close();
                } catch (IOException e2) {
                    query = e.toString();
                    e2.printStackTrace();
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private String tmdbSearch(String title, Integer page){
            title = title.replaceAll(" ", "+");
            if (title.length() > 0)
                return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings + "&page=" + page + "&query=" + title;
            else
                return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings + "&page=" + page;
        }


    }


    public static ArrayList<Movie> searchByActor(String actor){
        ArrayList<Movie> results = new ArrayList<Movie>();
        try {
            query = actor;
            executor.submit(new SearchByActorThread());
            do {
                results.add(loadedResults.poll(5, TimeUnit.SECONDS));
            }while(!loadedResults.isEmpty());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private static class SearchByActorThread implements Runnable {

        @Override
        public void run() {
            try{
                running = true;
                Gson gson = new Gson();

                System.out.println("Starting Search");
                PersonResults searchedActor =  gson.fromJson(
                        Unirest.get(tmdbSearchPeople(query))
                                .asJson()
                                .getBody()
                                .toString(),
                        PersonResults.class);

                Integer id = searchedActor.getId();

                System.out.println("Actor ID is " + id);

                MoviesByPersonResults movieResults = gson.fromJson(
                        Unirest.get(tmdbMoviesByPerson(id))
                                .asJson()
                                .getBody()
                                .toString(),
                        MoviesByPersonResults.class);

                loadedResults.addAll(movieResults.getResults());

            }catch(UnirestException e){
                System.out.println("Unirest Exception thrown");
            }
            finally {
                running = false;
            }
        }
    }

    public static void searchByActorFull(String actor, ArrayList<Movie> resultsPointer) throws InterruptedException{
        MovieLoader loader = new MovieLoader();
        results = resultsPointer;
        results = searchByActor(actor);
        loader.loadMovies(results);
    }

    public static String searchTest(String title){
        query = title;
        ArrayList<Movie> movieResults = new ArrayList<Movie>();
        try{
            MovieLoader loader = new MovieLoader();
            Gson gson = new Gson();
            URL search = new URL(omdbSearch(query, 1));
            System.out.println(search);
            HttpURLConnection searchCon = (HttpURLConnection) search.openConnection();

            searchCon.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(searchCon.getInputStream()));
            OmdbSearchResults results = gson.fromJson(reader, OmdbSearchResults.class);
            int pages = results.getTotal_pages();
            //System.out.println(pages);
            totalResults = results.getNumberofResults();
            if(pages>40){
                // Throw some sort of Exception
                return null;
            }
            else{
                for(int page = 1; page<=pages; page++){
                    currentPage = page;
                    search = new URL(omdbSearch(query,  page));
                    searchCon = (HttpURLConnection) search.openConnection();
                    searchCon.setRequestMethod("GET");
                    reader = new BufferedReader(new InputStreamReader(searchCon.getInputStream()));
                    results = gson.fromJson(reader, OmdbSearchResults.class);
                    //System.out.println(results);
                    ArrayList<Movie> moviesPage = results.getResults();
                    try{
                        loader.loadMoviebyTitle(collectTitles(results.getResults()));
                        for (Movie m : moviesPage){
                            //System.out.println(m.getTitle());
                            movieResults.add(loader.LoadedMovies.take());
                        }
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("In Method:" +movieResults.toString());
                return movieResults.toString();
            }
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

    public static void searchFull(String title, ArrayList<Movie> resultsPointer){
            query = title;
            results = resultsPointer;
            executor.submit(new SearchFullThread());
    }

    private static class SearchFullThread implements Runnable{

        @Override
        public void run() {
            running = true;
            try{
                MovieLoader loader = new MovieLoader();
                Gson gson = new Gson();
                URL search = new URL(omdbSearch(query, 1));
                System.out.println(search);
                HttpURLConnection searchCon = (HttpURLConnection) search.openConnection();
                searchCon.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(searchCon.getInputStream()));
                OmdbSearchResults newResults = gson.fromJson(reader, OmdbSearchResults.class);

                results = newResults.getResults();
                pages = newResults.getTotal_pages();
                totalResults = newResults.getNumberofResults();

                if(pages>40)
                    pages = 40;

                for(int page = 1; page<=pages; page++){
                    currentPage = page;
                    search = new URL(omdbSearch(query,  page));
                    searchCon = (HttpURLConnection) search.openConnection();
                    searchCon.setRequestMethod("GET");
                    reader = new BufferedReader(new InputStreamReader(searchCon.getInputStream()));
                    newResults = gson.fromJson(reader, OmdbSearchResults.class);
                    results.addAll(newResults.getResults());
                }
                loader.loadMovies(results);
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
            finally {
                running = false;
            }
        }
    }

    private static List<String> collectTitles(List<Movie> results){
        List<String> titles = new ArrayList<>();
        for (Movie m : results){
            titles.add(m.getTitle());
        }
        return titles;
    }

    //This is the class Gson parses to return the search results
    private class TmdbSearchResults{
        Integer page;
        Integer total_results;
        Integer total_pages;
        ArrayList<TmdbMovie> results;

        public String toString(){
            return "Page: " + page + "Total Results: " + total_results + ", Results: " + results.get(0).toString();
        }

        public Integer getTotal_pages() {
            return total_pages;
        }

        public Integer getNumberofResults(){
            return total_results;
        }

        public ArrayList<Movie> getResults(){
            ArrayList<Movie> searchResults = new ArrayList<Movie>();
            for(TmdbMovie movie : results){
                searchResults.add(movie.toMovie());
            }
            return searchResults;
        }
    }

    private class OmdbSearchResults {
        Integer total_pages;
        Boolean Response;
        Integer totalResults;
        ArrayList<Movie> Search;

        public String toString(){
            return "Response: " + Response+ ", Total Results: " + totalResults + ", Results: " + Search;
        }

        public Integer getTotal_pages() {
            total_pages = totalResults/10+1;
            return total_pages;
        }

        public Integer getNumberofResults(){
            return totalResults;
        }

        public ArrayList<Movie> getResults(){
            return Search;
        }
    }

    public class PersonResults{
        public class Actor{
            String name;
            Integer id;

            public String getName(){
                return name;
            }

            public Integer getId(){
                return id;
            }
        }

        ArrayList<Actor> results;

        public Integer getId(){
            return results.get(0).getId();
        }
    }

    public class MoviesByPersonResults{
        ArrayList<TmdbMovie> cast;

        public ArrayList<Movie> getResults(){
            ArrayList<Movie> searchResults = new ArrayList<Movie>();
            for(TmdbMovie movie : cast){
                searchResults.add(movie.toMovie());
            }
            return searchResults;
        }
    }


    public static String omdbSearch (String title, Integer page) {
        title = title.replaceAll(" ", "+");
        return omdbUrl +"apikey=" + omdbApiKey + "&s=" + title + "&page=" + page;
    }

    public static String tmdbSearch(String title, Integer page){
        title = title.replaceAll(" ", "+");
        if(title.length()>0)
            return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings+ "&page=" + page + "&query=" + title;
        else
            return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings+ "&page=" + page;
    }

    public static String tmdbSearchPeople(String name){
        name = name.replaceAll(" ", "+");
        return tmdbUrl + tmdbSearchPeopleUrl + tmdbApiKey + tmdbSettings+ "&query=" + name;
    }

    public static String tmdbMoviesByPerson(Integer id){
        return tmdbMoviesByPersonUrl + id + tmdbMovieCredits + tmdbApiKey + tmdbSettings;
    }


    public static boolean close(){
        try{
            reader.close();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
}
