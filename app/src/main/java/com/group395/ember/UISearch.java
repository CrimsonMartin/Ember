package com.group395.ember;


import java.util.ArrayList;

/**
 * Protocol for accessing the Movie Database from the GUI of the Ember app.
 * @version 1.2
 * @since 8-Mar-2019
 */

public class UISearch {

    private ArrayList<Filter> filters = new ArrayList<>(0);  // Filter list to hold all 3 possible filters.
    private String searchTerms;                                                 // The search terms to access the database with
    private static ArrayList<Movie> results;                                                // Results from an API call

    /**
     * Default constructor for a UISearch
     */
    protected UISearch() {
        // Creating basic filters as placeholders
        filters.add(new Filter(FilterType.ACTOR));
        filters.add(new Filter(FilterType.GENRE));
        filters.add(new Filter(FilterType.DIRECTOR));
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
        results = search();
    }

    /**
     * Default search method to make a Movie api call to get some amount of Movies (stores in results and returns).
     * @return List of Movies
     */
    public ArrayList<Movie> search() {
        MovieSearch m = new MovieSearch();
        results = applyFilters(m.searchFirstPage(String.join(" ", getSearch())));
        return results;
    }

    /**
     * Extended search method to make an API call. Gets "all" of the related results based on title then filters results.
     * @return List of Movies
     */
    public ArrayList<Movie> searchFull() {
        // Calls a full search and converts the keywords (String[]) to a single String separated by spaces/
        MovieSearch m = new MovieSearch();
        results = applyFilters(m.searchFull(String.join(" ", getSearch())));
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

    /** Sorts the Movies by checking if they are applicable to each filter.
     * @param rawList is the unfiltered List of Movies to sort
     * @return a filtered List of Movies.
     */
    public ArrayList<Movie> applyFilters(ArrayList<Movie> rawList) {
        ArrayList<Movie> filteredList = new ArrayList<Movie>();

        // Loops each filter for each movie to determine if they fit the filters.
        for (Filter filter : getFilters()) {

            if (filteredList.size() == 0) {
                for (Movie movie : rawList) {
                    if (filter.fitsFilter(movie))
                        filteredList.add(movie);
                }
            }

            // Narrowing in on remaining movies with the rest of the filters.
            else {
                for (Movie movie : filteredList) {
                    // If it does not fit the next filter, remove it.
                    if (!filter.fitsFilter(movie))
                        filteredList.remove(movie);
                }
            }

        }

        return filteredList;
    }

    protected static void searchFromButton(String input, boolean actorNotTitle) {
        System.out.println("Running searchFromButton(" + input + ", " + actorNotTitle + ")");
        try {
            if (actorNotTitle) {
                results = MovieSearch.searchByActorFull(input);
            } else {
                results = MovieSearch.searchFull(input);
            }
            System.out.println("Finished searchFromButton(" + input + ", " + actorNotTitle + ")");
        }catch(Exception e){
            System.out.println("Caught exception: " + e.toString());
        }
    }

    protected static Movie[] getTwo(int pagesSkipped){
        System.out.println("Running getTwo(" + pagesSkipped + ")");
        Movie[] output = new Movie[2];
        if(pagesSkipped * 2 < results.size()){ output[0] = results.get(pagesSkipped * 2); }
        if(pagesSkipped * 2 + 1 < results.size()){ output[1] = results.get(pagesSkipped * 2 + 1); }
        System.out.println("Finished getTwo(" + pagesSkipped + ")");
        return output;
    }
}
