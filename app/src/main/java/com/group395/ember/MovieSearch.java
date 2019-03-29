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

    private static String omdbApiKey = "33d1a530";
    private static String omdbUrl = "http://www.omdbapi.com/?";
    private static BufferedReader reader = null;

    public static searchResults search(String title){
        try{
            Gson gson = new Gson();
            openSearchConnection(title);
            searchResults results = gson.fromJson(reader, searchResults.class);
            return results;

        }
        catch (IOException e){
            System.out.println("Title search failed: " + e.getMessage());
            e.printStackTrace();
            close();
            return null;
        }

    }

    private static void openSearchConnection (String title) throws IOException{

        try{
            URL obj = new URL(omdbSearch(title));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (MalformedURLException e) {
            System.out.println("INVALID URL FORMAT");
            e.printStackTrace();
            close();
        }
    }

    //This is the class Gson parses to return the search results
    public class searchResults{
        ArrayList<Movie> Search;
        Integer totalResults;
        Boolean Response;

        public String toString(){
            return "Results: " + totalResults + ", Response: " + Response + ", Top Movie: " + Search.get(0).toString();
        }

        public Boolean getResponse() {
            return Response;
        }

        public Integer getNumberofResults(){
            return totalResults;
        }

        public ArrayList<Movie> getResults(){
            return Search;
        }
    }

    public static String omdbSearch(String title){
        title = title.replaceAll(" ", "+");
        return omdbUrl +"apikey=" + omdbApiKey + "&s=" + title + "&plot=full";
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
