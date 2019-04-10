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

    public static String query = "";
    public BlockingQueue<Movie> loadedResults = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    public int totalResults = 0;
    public int currentPage = 0;

    private static int MAXNUMMOVIES = 1000;
    private static int MAXNUMTHREADS = 8;
    private BlockingQueue<String> movietitles = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    private ExecutorService executor = Executors.newFixedThreadPool(MAXNUMTHREADS);
    private ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
    private static boolean running = false;


    private static BufferedReader reader = null;

    // Returns a list of movies
    public ArrayList<Movie> searchFirstPage(String title) {
        ArrayList<Movie> results = new ArrayList<Movie>();
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

    private class SearchFirstPageThread implements Runnable {

        @Override
        public void run() {
            try {
                running = true;
                Gson gson = new Gson();
                URL obj = new URL(tmdbSearch(query, 1));
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                SearchResults results = gson.fromJson(reader, SearchResults.class);
                loadedResults.addAll(results.getResults());
            } catch (MalformedURLException e) {
                System.out.println("Title search failed: " + e.getMessage());
                e.printStackTrace();
                try {
                    reader.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                running = false;
            }
        }

        private String tmdbSearch(String title, Integer page) {
            title = title.replaceAll(" ", "+");
            if (title.length() > 0)
                return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings + "&page=" + page + "&query=" + title;
            else
                return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings + "&page=" + page;
        }
    }




        public ArrayList<Movie> searchByActor(String actor){
            ArrayList<Movie> results = new ArrayList<Movie>();
            try {
                //System.out.println("Loader thread starting");
                String response = null;
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

    private class SearchByActorThread implements Runnable {

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

    public ArrayList<Movie> searchByActorFull(String actor) throws InterruptedException{
        MovieLoader loader = new MovieLoader();
        ArrayList<Movie> movies = searchByActor(actor);

        ArrayList<String> titles = new ArrayList<>();
        for (Movie movie : movies){
            //System.out.println("Title getting added: " + movie.getTitle());
            titles.add(movie.getTitle());
        }
        loader.loadMoviebyTitle(titles);

        System.out.println("Loading " + movies.size() + " movies");

        ArrayList<Movie> returned = new ArrayList<>();

        // This blocks until all the movies have been loaded
        // if it's being executed from the main thread, then it needs to create another thread
        // that calls this function instead of directly calling it.
        while (returned.size() < movies.size()){
            returned.add(loader.loadedmovies.poll(5, TimeUnit.SECONDS));
        }

        return returned;
    }

    public ArrayList<Movie> searchFull(String title){
        ArrayList<Movie> results = new ArrayList<Movie>();
        try{
            query = title;
            executor.submit(new SearchFullThread());
            do {
                results.add(loadedResults.poll(5, TimeUnit.SECONDS));
                System.out.println(currentPage+" "+totalResults);
            }while(!loadedResults.isEmpty() || running);
            System.out.println("done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private class SearchFullThread implements Runnable{

        @Override
        public void run() {
            running = true;
            try{
                MovieLoader loader = new MovieLoader();
                Gson gson = new Gson();
                URL tmdb = new URL(tmdbSearch(query, 1));
                HttpURLConnection tmdbCon = (HttpURLConnection) tmdb.openConnection();

                tmdbCon.setRequestMethod("GET");

                reader = new BufferedReader(new InputStreamReader(tmdbCon.getInputStream()));
                SearchResults results = gson.fromJson(reader, SearchResults.class);

                int pages = results.getTotal_pages();
                totalResults = results.getNumberofResults();
                if(pages>20){
                    // Throw some sort of Exception
                }
                else{
                    for(int page = 1; page<=pages; page++){
                        currentPage = page;
                        tmdb = new URL(tmdbSearch(query,  page));
                        tmdbCon = (HttpURLConnection) tmdb.openConnection();
                        tmdbCon.setRequestMethod("GET");
                        reader = new BufferedReader(new InputStreamReader(tmdbCon.getInputStream()));
                        results = gson.fromJson(reader, SearchResults.class);

                        ArrayList<Movie> moviesPage = results.getResults();
                        try{
                            loader.loadMoviebyTitle(collectTitles(results.getResults()));
                            for (Movie m : moviesPage){
                                //System.out.println(m.getTitle());
                                loadedResults.add(loader.loadedmovies.take());
                            }
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
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

        private String tmdbSearch(String title, Integer page){
            title = title.replaceAll(" ", "+");
            if(title.length()>0)
                return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings+ "&page=" + page + "&query=" + title;
            else
                return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings+ "&page=" + page;
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
    class SearchResults{
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
