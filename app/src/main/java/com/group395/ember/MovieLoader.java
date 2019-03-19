package com.group395.ember;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class MovieLoader {

    private static String omdbApiKey = "33d1a530";
    private static String omdbUrl = "http://www.omdbapi.com/?";

    private static String utelliAPIKey = "6bff01b396msh0f92aae4b854e96p1277f2jsna247a9a391a8";
    private static String utelliUrl = "https://utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com/lookup?term=";


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

    private String addApiKey(String omdbUrl){
        return omdbUrl +"apikey=" + omdbApiKey;
    }

    String omdbUrlFromTitle (String title){
        title = title.replaceAll(" ", "+");
        return addApiKey(omdbUrl) + "&t=" + title + "&plot=full";
    }

    private void openTitleConnection (String title) throws IOException{

        try{
            URL obj = new URL(omdbUrlFromTitle(title));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        } catch (MalformedURLException e) {
            System.out.println("INVALID URL FORMAT");
            e.printStackTrace();
            close();
        }
    }

    private String createUtelliSearchURL(String movieTitle){
        return utelliUrl + movieTitle.replaceAll(" ", "+").toLowerCase();
    }

    public void loadPlatforms(Movie m){
        try{
            HttpResponse<JsonNode> response = Unirest.get(createUtelliSearchURL(m.getTitle()))
                .header("X-RapidAPI-Key", utelliAPIKey)
                .asJson();

            loadPlatforms(m,response.getBody().toString());

        }catch (UnirestException e){
            System.out.println("loading platform failed, exception " + e.getMessage());
        }
    }

    void loadPlatforms(Movie m, String json){
        m.addPlatforms(json);
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
