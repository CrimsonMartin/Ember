package com.group395.ember;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


public class MovieLoader {

    private static String omdbApiKey = "33d1a530";
    private static String searchUrl = "http://www.omdbapi.com/?";

    private BufferedReader reader;

    List<Movie> loadMovies(UISearch uiSearch, Integer n) throws UnsupportedOperationException{
        //TODO
        //How does this know what kind of movies to search for?
        throw new UnsupportedOperationException();
    }

    public Movie loadMovie( UISearch uiSearch ){
        return loadMoviebyTitle(uiSearch.getSearch());
    }

    Movie loadMoviebyTitle(String title){
        try{
            openTitleConnection(title);
            return Movie.parseFromJson(reader);
        }
        catch (IOException e){
            System.out.println("Title search failed: " + e.getMessage());
            e.printStackTrace();
            close();
            return null;
        }

    }

    private String addApiKey(String searchUrl){
        return searchUrl +"apikey=" + omdbApiKey;
    }

    String searchUrlFromTitle (String title){
        title = title.replaceAll(" ", "+");
        return addApiKey(searchUrl) + "&t=" + title + "&plot=full";
    }

    private void openTitleConnection (String title) throws IOException{

        try{
            URL obj = new URL(searchUrlFromTitle(title));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        } catch (MalformedURLException e) {
            System.out.println("INVALID URL FORMAT");
            e.printStackTrace();
        }
    }

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
