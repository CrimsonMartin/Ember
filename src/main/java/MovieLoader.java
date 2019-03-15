import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.io.*;

public class MovieLoader {

    private String omdbApiKey = "33d1a530";
    private String searchUrl = "http://www.omdbapi.com/?";

    private BufferedReader reader;


    public List<Movie> loadMovies(UISearch uiSearch, Integer n) throws UnsupportedOperationException{
        //TODO
        throw new UnsupportedOperationException();
    }

    public Movie loadMovie( UISearch uiSearch ){
        return loadMoviebyTitle(uiSearch.getSearch());
    }

    public Movie loadMoviebyTitle(String title){
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

    String addApiKey(String searchUrl){
        return searchUrl +"apikey=" + omdbApiKey;
    }

    String searchUrlFromTitle (String title){
        title = title.replaceAll(" ", "+");
        return addApiKey(searchUrl) + "&t=" + title + "&plot=full";
    }

    void openTitleConnection (String title) throws IOException{

        try{
            URL obj = new URL(searchUrlFromTitle(title));
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        } catch (MalformedURLException e) {
            System.out.println("INVALID URL FORMAT");
            e.printStackTrace();

        } catch(IOException e){
            throw e;
        }
    }

    public boolean close(){
        try{
            reader.close();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }

}
