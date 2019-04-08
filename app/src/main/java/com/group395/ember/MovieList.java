package com.group395.ember;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MovieList {

    private Integer N = null;
    private MovieLoader loader = new MovieLoader();
    private UISearch uisearch = null;
    private List<Movie> activeList = new ArrayList<>();

    /**
     *
     * @return true if the movie loader was closed successfully, and false otherwise
     */
    public boolean closeMovieLoader(){
        return loader.close();
    }

    public void sort(){
        //TODO
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

    /**
     *
     * @return List of Movies that are in the Active List,
     *  ie movies that match the search criteria the user has input
     */
    List<Movie> getActiveList(){
        activeList.sort(new MovieIMDBRating());
        return activeList;
    }

    void add(Movie m){
        activeList.add(m);
    }

    void addAll(List<Movie> ml){ activeList.addAll(ml); }

    private static class MovieIMDBRating implements Comparator<Movie> {

        @Override
        public int compare(Movie m1, Movie m2) {
            return (int)(m1.getImdbRating() - m2.getImdbRating());
        }

    }

}



