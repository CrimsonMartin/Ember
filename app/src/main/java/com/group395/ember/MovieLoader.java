package com.group395.ember;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

class MovieLoader {

    private static int MAXNUMMOVIES = 1000;

    private static String omdbApiKey = "db5b96c2";
    private static String omdbUrl = "http://www.omdbapi.com/?";

    private static String utelliAPIKey = "6bff01b396msh0f92aae4b854e96p1277f2jsna247a9a391a8";
    private static String utelliUrl = "https://utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com/lookup?term=";

    private BufferedReader reader;


    @Deprecated
    List<Movie> loadMovies(UISearch uiSearch, Integer n) throws UnsupportedOperationException{
        //How does this know what kind of movies to search for?
        throw new UnsupportedOperationException();
    }

    /**
     * @param uiSearch the search input from the user, attempting to find a particular movie
     * @return the movie object if the loading was successful, and null otherwise
     */
    public Movie loadMovie( UISearch uiSearch ){
        return loadMoviebyTitle(uiSearch.getSearch());
    }

    /**
     *
     * @param title the string representation of the title of the movie we want to know more about
     * @return the movie object if the loading was successful, and null otherwise
     */
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

    /**
     *
     * @param titles a list of movie titles
     * @return the list of movies that correspond to the
     */
    public List<Movie> loadMoviebyTitle(List<String> titles) throws IllegalArgumentException, IOException, InterruptedException{

        if (titles.size() > MAXNUMMOVIES){
                throw new IllegalArgumentException("Can't search for more than " + titles.size()
                        + " movies at once");
        }

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(titles.size());

        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        try {
            // create an array of URIs to perform GETs on
            String[] urisToGet = {
                    "http://hc.apache.org/",
                    "http://hc.apache.org/httpcomponents-core-ga/",
                    "http://hc.apache.org/httpcomponents-client-ga/",
            };

            // create a thread for each URI
            GetThread[] threads = new GetThread[urisToGet.length];
            for (int i = 0; i < threads.length; i++) {
                HttpGet httpget = new HttpGet(urisToGet[i]);
                threads[i] = new GetThread(httpclient, httpget, i + 1);
            }

            // start the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].start();
            }

            // join the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].join();
            }

        } finally {
            httpclient.close();
        }
    }

    private List<String> omdbUrlFromTitle (List<String>  titles){
        List<String> urls = new
    }

    private String omdbUrlFromTitle (String title){
        title = title.replaceAll(" ", "+");
        return omdbUrl +"apikey=" + omdbApiKey + "&t=" + title + "&plot=full";
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

    /**
     *
     * @param m Movie object to load the available platforms for
     */
    void loadPlatforms(Movie m){
        try{
            HttpResponse<JsonNode> response = Unirest.get(createUtelliSearchURL(m.getTitle()))
                .header("X-RapidAPI-Key", utelliAPIKey)
                .asJson();

            loadPlatforms(m,response.getBody().toString());

        }catch (UnirestException e){
            System.out.println("loading platform failed, exception " + e.getMessage());
        }
    }

    /**
     *
     * @param m the movie to load the available platforms for
     * @param json String representation of the json response from requesting the available platforms from UTelli
     */
    void loadPlatforms(Movie m, String json){
        m.addPlatforms(json);
    }

    /**
     *
     * @return boolean whether or not the connection was closed successfuly
     */
    boolean close(){
        try{
            reader.close();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }


    private static class GetThread extends Thread {

        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private final int id;

        public GetThread(CloseableHttpClient httpClient, HttpGet httpget, int id) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            this.id = id;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {
            try {
                System.out.println(id + " - about to get something from " + httpget.getURI());
                CloseableHttpResponse response = httpClient.execute(httpget, context);
                try {
                    System.out.println(id + " - get executed");
                    // get the response body as an array of bytes
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        byte[] bytes = EntityUtils.toByteArray(entity);
                        System.out.println(id + " - " + bytes.length + " bytes read");
                    }
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println(id + " - error: " + e);
            }
        }

    }

}
