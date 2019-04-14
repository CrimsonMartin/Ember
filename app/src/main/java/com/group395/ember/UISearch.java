package com.group395.ember;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Protocol for accessing the Movie Database from the GUI of the Ember app.
 * @version 1.2
 * @since 8-Mar-2019
 */

public class UISearch {

    private ArrayList<Filter> filters = new ArrayList<>(0);  // Filter list to hold all 3 possible filters.
    private String searchTerms; // The search terms to access the database with
    private List<Movie> results = new ArrayList<>();  // Results from an API call
    private Integer pageNumMoviesReturned = 6;
    private MovieSearch currentSearch;
    private Integer FullNumMoviesReturned = 40;

    /**
     * Default constructor for a UISearch
     */
    protected UISearch() {
        // Creating basic filters as placeholders
        filters.add(new Filter(FilterType.ACTOR));
        filters.add(new Filter(FilterType.GENRE));
        filters.add(new Filter(FilterType.DIRECTOR));
    }

    /**
     * Adds a new filter to the current search. If there exists a Filter with the same FilterType,
     * the old Filter will be overwritten.
     * @param newFilter is a new Filter to use.
     */
    public void addFilter(Filter newFilter) {
        for (int i = 0; i < 3; i++) {
            // If the two filters have the same enum type, replace the old one
            if (getFilters().get(i).getFilterType().equals(newFilter.getFilterType())) {
                getFilters().set(i, newFilter);
                return;
            }
        }
    }

    /**
     * Returns the current filter being used for searching
     * @return Filter type representing current Filter
     */
    public ArrayList<Filter> getFilters() {
        return filters;
    }

    /**
     * Returns the user's search criteria as a String
     * @return String containing search terms
     */
    public String getSearch() {
        return searchTerms;
    }

    /**
     * Clears the current search field. Should be called from the GUI upon clearing the search bar
     */
    public void clearSearch() {
        searchTerms = "";
    }


    /**
     * Sets a new String to use for searching
     * @param newSearch is a String containing new search terms
     */
    public void setSearch(String newSearch) {
        searchTerms = newSearch;
    }

    /**
     * Default search method to make a Movie api call to get some amount of Movies (stores in results and returns).
     * @param MoviesNeeded is the number of Movies Needed to operate
     * @return List of Movies
     */
    public List<Movie> search(int MoviesNeeded) throws InterruptedException{
        while(results.size() < MoviesNeeded && currentSearch.totalResults != 0 && !currentSearch.isExhausted()){
            Movie current = currentSearch.results.poll(2, TimeUnit.SECONDS);
            if(current != null && fitsFilters(current))
                results.add(current);
        }
        return results;
    }

    /**
     * Returns an array of Movies from index start to index end
     * @param start left bound of the array.
     * @param end right right of the array.
     * @return array of Movies
     */
    public Movie[] getMovies(int start, int end) {
        Movie[] arr = new Movie[start - end + 1];
        try {
            for (int i = start; i <= end; i++) {
                arr[i] = results.get(i);
            }
        }
        catch(IndexOutOfBoundsException e) {
            return null;
        }

        return arr;
    }

    /**
     * Returns the Movie at the given index.
     * @param i the index of Movie to return.
     * @return Movie
     */
    public Movie getMovie(int i) {
        try {
            return results.get(i);
        }
        catch(IndexOutOfBoundsException e) {
            System.out.println("OUT OF RANGE - RETURNING @ 0");
            return null;
        }
    }

    public boolean fitsFilters(Movie m) {
        return filters.get(0).fitsFilter(m) && filters.get(1).fitsFilter(m) && filters.get(2).fitsFilter(m);
    }

    /** Sorts the Movies by checking if they are applicable to each filter.
     * @return a filtered List of Movies.
     */
    public List<Movie> applyFilters(Integer moviesNeeded) throws InterruptedException {
        int additionalMovies = 0;
        do{
            if(additionalMovies>0)
                search(moviesNeeded+additionalMovies);
            for (Filter filter : getFilters()) {
                for (Movie movie : results) {
                    // If it does not fit the next filter, remove it.
                    if (!filter.fitsFilter(movie))
                        results.remove(movie);
                }
            }
            additionalMovies = additionalMovies + 2;
        }while(results.size() < moviesNeeded);
        return results;
    }

    // Needs to be redone to actually use the filters.
    protected void searchFromButton(String input, boolean actorNotTitle) {
        currentSearch = new MovieSearch();
        try {
            if (actorNotTitle) {
                currentSearch.searchByActor(searchTerms);
            } else {
                currentSearch.searchFull(searchTerms);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    protected Movie[] getTwo(int pagesSkipped) {
        Movie[] output;
        try{
            int moviesNeeded = pagesSkipped * 2 + 2;
            this.search(moviesNeeded);
            //this.applyFilters(moviesNeeded);
            output = new Movie[2];
            output[0] = results.get(pagesSkipped * 2);
            output[1] = results.get(pagesSkipped * 2 + 1);
        }
        catch (IndexOutOfBoundsException | InterruptedException e){
            output = new Movie[]{null, null};
        }

        return output;
    }

    protected void resetResults(){
        results.clear();
    }
}
