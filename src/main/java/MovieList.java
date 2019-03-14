import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MovieList {

    private Integer N = null;
    private MovieLoader loader = new MovieLoader();
    private UISearch uisearch = null;
    private List<Movie> activeList = new ArrayList<>();

    public boolean closeMovieLoader(){
        return loader.close();
    }


    public void sort(){
        //TODO
        return;
    }


    public Integer getN() {
        return N;
    }

    public void setN(Integer n) {
        N = n;
    }

    public void setUISearch(UISearch uiSearch){
        uisearch = uiSearch;
    }

    public List<Movie> getActiveList(){
        activeList.sort(new MovieIMDBRating());
        return activeList;
    }

    protected void add(Movie m){
        activeList.add(m);
    }

    protected  void addAll(List<Movie> ml){ activeList.addAll(ml); }

    private static class MovieIMDBRating implements Comparator<Movie> {

        @Override
        public int compare(Movie m1, Movie m2) {
            return m1.getImdbRating() - m2.getImdbRating();
        }

    }

}



