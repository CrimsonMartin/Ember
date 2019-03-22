package com.group395.ember;


import java.util.ArrayList;
import java.util.List;

/**
 * Protocol for accessing the Movie Database from the GUI of the Ember app.
 * @version 1.0
 * @since 8-Mar-2019
 */

public class UISearch {

    private List<Filter> filters = new List<Filter>(3);  // Filter list to hold all 3 possible filters.
    private String searchTerms;                          // The search terms to access the database with


    /**
     * Default constructor for a UISearch
     */
    public UISearch() {
        // Creating basic filters as placeholders
        filters.set(0, new Filter(FilterType.ACTOR));
        filters.set(1, new Filter(FilterType.GENRE));
        filters.set(2, new Filter(FilterType.DIRECTOR));
        System.out.println("Init UISearch");
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
    public List<Filter> getFilters() {
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
     * Uses the current search Filter and String to access the database
     * @return Array of movies to implement as MovieTiles later on
     */
    public List<Movie> search() {
        int moviesToLoad = 5;
        return search(moviesToLoad);
        // return movies;
    }

    /**
     * Overloads the default search method to load n number of movies rather than just 5.
     * @param n is the number of movies to load
     * @return Array of movies to implement as MovieTiles later on
     */
    public List<Movie> search(int n) {
        MovieLoader loader = new MovieLoader();
        //TODO I'm turning this to load one movie, because I need some clarification on
        // how the load N movies knows what sort of movies to load
        // List<Movie> movies = loader.loadMovies(this, n);
        List<Movie> movies = new ArrayList<>();
        movies.add(loader.loadMovie(this));
        return movies;
    }

}
