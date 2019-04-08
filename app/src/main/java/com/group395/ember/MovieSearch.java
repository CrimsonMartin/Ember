package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MovieSearch {

    private static String tmdbApiKey = "2798eab352dd4b7d99e4a0f825802ff5";
    private static String tmdbUrl = "https://api.themoviedb.org/3/";
    private static String tmdbSearchUrl = "search/movie?api_key=";
    private static String tmdbSearchPeopleUrl = "search/person?api_key=";
    private static String tmdbMoviesByPersonUrl = "https://api.themoviedb.org/3/person/";
    private static String tmdbMovieCredits = "/movie_credits?api_key=";
    private static String tmdbSettings = "&language=en-US&include_adult=false";


    private static BufferedReader reader = null;

    // Returns a list of movies
    public static ArrayList<Movie> searchFirstPage(String title){
        try{
            Gson gson = new Gson();
            URL obj = new URL(tmdbSearch(title, 1));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            SearchResults results = gson.fromJson(reader, SearchResults.class);
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

    public static ArrayList<Movie> searchByActor(String actor){
        try{
            Gson gson = new Gson();
            URL obj = new URL(tmdbSearchPeople(actor));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            PersonResults results = gson.fromJson(reader, PersonResults.class);

            Integer id = results.getId();
            URL obj2 = new URL(tmdbMoviesByPerson(id));
            HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
            con2.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            MoviesByPersonResults movieResults = gson.fromJson(reader, MoviesByPersonResults.class);
            return movieResults.getResults();
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

    public static ArrayList<Movie> searchByActorFull(String actor){
        MovieLoader loader = new MovieLoader();
        ArrayList<Movie> movies = searchByActor(actor);
        for(int i = 0; i<movies.size(); i++){
            movies.set(i, loader.loadMoviebyTitle(movies.get(i).getTitle()));
        }
        return movies;
    }

    public static ArrayList<Movie> searchFull(String title){
        try{
            MovieLoader loader = new MovieLoader();
            Gson gson = new Gson();
            URL tmdb = new URL(tmdbSearch(title, 1));
            HttpURLConnection tmdbCon = (HttpURLConnection) tmdb.openConnection();

            tmdbCon.setRequestMethod("GET");
            ArrayList<Movie> movieResults = new ArrayList<Movie>();

            reader = new BufferedReader(new InputStreamReader(tmdbCon.getInputStream()));
            SearchResults results = gson.fromJson(reader, SearchResults.class);

            int pages = results.getTotal_pages();
            if(pages>20){
                return null;
            }
            else{
                for(int page = 1; page<=pages; page++){
                    tmdb = new URL(tmdbSearch(title,  page));
                    tmdbCon = (HttpURLConnection) tmdb.openConnection();
                    tmdbCon.setRequestMethod("GET");
                    reader = new BufferedReader(new InputStreamReader(tmdbCon.getInputStream()));
                    results = gson.fromJson(reader, SearchResults.class);

                    ArrayList<Movie> moviesPage = results.getResults();

                    for(int i = 0; i<moviesPage.size(); i++){
                        moviesPage.set(i, loader.loadMoviebyTitle(moviesPage.get(i).getTitle()));
                    }
                    movieResults.addAll(moviesPage);
                }
            }
            return movieResults;
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
    public class SearchResults{
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
