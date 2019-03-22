package com.group395.ember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Filter {

    // Is a list the best way to represent this?
    private String[] filterKeywords;
    private FilterType filterType;

    public FilterType getFilterType() {
        return filterType;
    }

    private Logger log = new Logger();

    /**
     * Creates a new filter with the given Movie's genres as the filterKeywords and type.
     * Defaults to genre filter type.
     * @param movie to base the filter off of.
     */
    private Filter(Movie movie) {
        filterType = FilterType.GENRE;
    }

    /**
     * Creates an empty filter given a FilterType.
     * @param filterType to use for the new Filter
     */
    private Filter(FilterType filterType) { this.filterType = filterType; }

    /**
     * Creates and retuns a Filter based on a Movie's genre, directors, and actors.
     * @param movie is the Movie object to build the Filter off of.
     * @return a new Filter object to apply to searches.
     */
    public Filter setFilter(Movie movie) {
        return new Filter(movie);
    }


    // All of these pretty much do the same thing: Change the filter to the method name and fill in the keywords
    public void setDirectors(String[] directors) {
        filterType = FilterType.DIRECTOR;
        filterKeywords = directors;
    }

    public void setActors(String[] actors) {
        filterType = FilterType.ACTOR;
        filterKeywords = actors;
    }

    public void setGenres(String[] genres) {
        filterType = FilterType.GENRE;
        filterKeywords = genres;
    }


    /**
     * Checks if a Movie fits the current filter by comparing the filterKeywords of both movies
     * @param movie is the Movie to check
     * @return true if the Movie fits, false if it doesn't.
     */
    public boolean fitsFilter(Movie movie) {

       List<String> compareTo = new ArrayList<>();

        switch(filterType){
            case FilterType.ACTOR:
                compareTo.addAll(movie.getActors());
                break;
            case FilterType.GENRE:
                compareTo.addAll(movie.getGenre());
            break;
            case FilterType.DIRECTOR:
                compareTo.add(movie.getDirector());
                break;
            default:
                throw new NullPointerException("This Filter has no filter type set");
        }

        for (String s : compareTo) {
            for (String s2 : filterKeywords) {
                if (s.equals(s2))
                    return true;
            }
        }
        return false;
    }
}