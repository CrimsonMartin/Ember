package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static String query = "";
    private static Integer MAXNUMMOVIES = 40;
    public static BlockingQueue<Movie> results = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    public static ArrayList<Movie> toLoad;
    //public static BlockingQueue<Movie> loadedResults = new ArrayBlockingQueue<>(MAXNUMMOVIES);
    public static int totalResults = 0;
    public static int pages = 0;
    public static int currentPage = 0;
    public static boolean running = false;

    //private static SearchFirstPageThread firstPage = new SearchFirstPageThread();


    private static BufferedReader reader = null;

    /*
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
            } while (!loadedResults.isEmpty());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static ArrayList<Movie> searchByActor(String actor) {
        ArrayList<Movie> results = new ArrayList<Movie>();
        try {
            query = actor;
            executor.submit(new SearchByActorThread());
            do {
                results.add(loadedResults.poll(5, TimeUnit.SECONDS));
            } while (!loadedResults.isEmpty());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    private static class SearchByActorThread implements Runnable {

        @Override
        public void run() {
            try {
                Gson gson = new Gson();

                System.out.println("Starting Search");
                PersonResult searchedActor = gson.fromJson(
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

            } catch (UnirestException e) {
                System.out.println("Unirest Exception thrown");
            } finally {
                running = false;
            }
        }
    }


    public static void searchByActorFull(String actor, ArrayList<Movie> resultsPointer) throws InterruptedException {
        MovieLoader loader = new MovieLoader();
        results = resultsPointer;
        toLoad = searchByActor(actor);
        executor.submit(new SearchByActorFullThread());
    }
    */

    private static class SearchByActorFullThread implements Runnable {

        @Override
        public void run() {
            MovieLoader loader = new MovieLoader();
            List<Future<Movie>> futures = loader.loadMovies(toLoad);
            for (Future<Movie> future : futures) {
                try {
                    results.add(future.get(3, TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void searchFull(String title) {
        executor.submit(new SearchFullThread(title));
        //while(running){
    }

    private static class SearchFullThread implements Runnable {

        private String query;

        public SearchFullThread(String q){
            query = q;
        }

        @Override
        public void run() {
            running = true;

            OmdbSearchResults searchResults = null;
            List<Future<Movie>> loaded = new ArrayList<>();

            try {

                //TODO rewrite to a single for loop
                /*Gson gson = new Gson();

                String searchurl = omdbSearch(query, 1);

                HttpResponse<JsonNode> response = Unirest.get(searchurl).asJson();
                String responsebodystring = response.getBody().toString();
                searchResults = gson.fromJson(responsebodystring, OmdbSearchResults.class);
                results.addAll(searchResults.getResults());
                loaded.addAll(loader.loadMovies(searchResults.getResults()));
                */

                Gson gson = new Gson();
                URL obj = new URL(omdbSearch(query, 1));
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                /*
                String line = "";
                while((line = reader.readLine()) != null){
                    System.out.println("-"+line);
                }*/
                searchResults = gson.fromJson(reader, OmdbSearchResults.class);
                loaded.addAll(loader.loadMovies(searchResults.getResults()));
                reader.close();
            //} catch (UnirestException e) {
                //System.out.println("Title search failed: " + e.getMessage());
                //e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            Objects.requireNonNull(searchResults);

            pages = searchResults.getTotal_pages();
            totalResults = searchResults.getNumberofResults();

            pages = pages > 10 ? 10 : pages;

            List<Future<Movie>> loaded2;

            try {
                for (int p = 2; p < pages; p++) {
                    Gson gson = new Gson();
                    URL obj = new URL(omdbSearch(query, p));
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    searchResults = gson.fromJson(reader, OmdbSearchResults.class);
                    loaded.addAll(loader.loadMovies(searchResults.getResults()));             System.out.println("--"+searchResults.toString());
                    loaded.addAll(loader.loadMovies(searchResults.getResults()));
                    System.out.println("-load- "+p+" of "+pages+" "+ loaded);
                }
            } catch (Exception e) {
                System.out.println("Title search failed: " + e.getMessage());
                e.printStackTrace();
            }
            for (int i=0; i<loaded.size(); i++) {
                try {
                    System.out.println("results: "+results);
                    results.add(loaded.get(i).get(3, TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("done");

            running = false;

        }
    }


    private static List<String> collectTitles(List<Movie> results) {
        List<String> titles = new ArrayList<>();
        for (Movie m : results) {
            titles.add(m.getTitle());
        }
        return titles;
    }

    //This is the class Gson parses to return the search results
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
    }

    public class PersonResults {
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
    }

    public class MoviesByPersonResults {
        ArrayList<TmdbMovie> cast;

        public ArrayList<Movie> getResults() {
            ArrayList<Movie> searchResults = new ArrayList<Movie>();
            for (TmdbMovie movie : cast) {
                searchResults.add(movie.toMovie());
            }
            return searchResults;
        }
    }

    public static String omdbSearch(String title, Integer page) {
        title = title.replaceAll(" ", "+");
        return omdbUrl + "apikey=" + omdbApiKey + "&s=" + title + "&page=" + page;
    }

    public static String tmdbSearch(String title, Integer page) {
        title = title.replaceAll(" ", "+");
        if (title.length() > 0)
            return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings + "&page=" + page + "&query=" + title;
        else
            return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings + "&page=" + page;
    }

    public static String tmdbSearchPeople(String name) {
        name = name.replaceAll(" ", "+");
        return tmdbUrl + tmdbSearchPeopleUrl + tmdbApiKey + tmdbSettings + "&query=" + name;
    }

    public static String tmdbMoviesByPerson(Integer id) {
        return tmdbMoviesByPersonUrl + id + tmdbMovieCredits + tmdbApiKey + tmdbSettings;
    }

    /*
    private static class SearchFirstPageThread implements Runnable {

        @Override
        public void run() {
            OmdbSearchResults results;

            try {

                results = new Gson()
                        .fromJson(Unirest.get(omdbSearch(query, 1))
                                        .asJson()
                                        .getBody()
                                        .toString(),
                                OmdbSearchResults.class);

                loadedResults.addAll(results.getResults());

            } catch (UnirestException e) {
                System.out.println("Title search failed: " + e.getMessage());
                e.printStackTrace();
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
    */

}

