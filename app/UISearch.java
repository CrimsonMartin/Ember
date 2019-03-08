/**
 * Protocol for accessing the Movie Database from the GUI of the Ember app.
 * @version 1.0
 * @since 8-Mar-2019
 */

public class UISearch {

    private Filter filter;          // The filter to apply to the current search criteria
    private String searchTerms;     // The search terms to access the database with


    /**
     * Default constructor for a UISearch
     */
    public UISearch() {
        System.out.println("New UISearch");
    }

    /**
     * Sets a new filter for application to search criteria
     * @param newFilter is the new Filter object to use for searching
     */
    public void setFilter(Filter newFilter) {
        filter = newFilter;
    }

    /**
     * Returns the current filter being used for searching
     * @return Filter type representing current Filter
     */
    public Filter getFilter() {
        return filter;
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
    public Movie[] search() {
        int moviesToLoad = 5;
        MovieLoader loader = new MovieLoader();
        loader.open();
        Movie[] movies = loader.loadMovies(this, moviesToLoad);
        loader.close();

        return movies;
    }

    /**
     * Overloads the default search method to load n number of movies rather than just 5.
     * @param n is the number of movies to load
     * @return Array of movies to implement as MovieTiles later on
     */
    public Movie[] search(int n) {
        MovieLoader loader = new MovieLoader();
        loader.open();
        Movie[] movies = loader.loadMovies(this, n);
        loader.close();

        return movies;
    }

}