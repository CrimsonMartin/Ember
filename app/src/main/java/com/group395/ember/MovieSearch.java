package com.group395.ember;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.Gson;

public class MovieSearch {

    private static String tmdbApiKey = "2798eab352dd4b7d99e4a0f825802ff5";
    private static String tmdbUrl = "https://api.themoviedb.org/3/";
    private static String tmdbSearchUrl = "search/movie?api_key=";
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

    public class TmdbMovie{
        Integer vote_count;
        Integer id;
        Boolean video;
        String vote_average;
        String title;
        String popularity;
        String poster_path;
        String original_language;
        String originial_title;
        ArrayList<Integer> genre_ids;

        public Movie toMovie(){
            String baseImageURL = "https://image.tmdb.org/t/p/w500";
            Movie movie = new Movie(title);
            movie.setPoster(baseImageURL+poster_path);
            return movie;
        }
    }

    public static String tmdbSearch(String title, Integer page){
        title = title.replaceAll(" ", "+");
        return tmdbUrl + tmdbSearchUrl + tmdbApiKey + tmdbSettings+ "&page=" + page + "&query=" + title;
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
