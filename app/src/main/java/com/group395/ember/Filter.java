package com.group395.ember;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.group395.ember.FilterType.*;

public class Filter {

    private Set<String> filterKeywords = new LinkedHashSet<>();
    private FilterType filterType;

    public FilterType getFilterType() {
        return filterType;
    }

    /**
     * Creates an empty filter given a FilterType.
     * @param filterType to use for the new Filter
     */
    public Filter(FilterType filterType) { this.filterType = filterType; }


    // All of these pretty much do the same thing: Change the filter to the method name and fill in the keywords
    public void setDirectors(ArrayList<String> directors) {
        filterType = DIRECTOR;
        filterKeywords = new LinkedHashSet<>(directors);
    }

    public void setActors(ArrayList<String> actors) {
        filterType = ACTOR;
        filterKeywords = new LinkedHashSet<>(actors);
    }

    public void setGenres(ArrayList<String> genres) {
        filterType = GENRE;
        filterKeywords = new LinkedHashSet<>(genres);
    }

    public Set<String> getKeywords(){
        return filterKeywords;
    }

    public void add(Collection<String> input){
        filterKeywords.addAll(input);
    }

    public void add(String input){filterKeywords.add(input);}

    /**
     * Checks if a Movie fits the current filter by comparing the filterKeywords of both movies
     * @param m is the Movie to check
     * @return true if the Movie fits, false if it doesn't.
     */
    public boolean fitsFilter(Movie m) {

        final Movie movie = m;
        List<String> compareTo;

        switch(filterType){
            case ACTOR:
                compareTo = movie.getActors();
                break;
            case GENRE:
                compareTo = movie.getGenre();
                break;
            case DIRECTOR:
                compareTo = new ArrayList<String>() {{
                    add(movie.getDirector());
                }};
                break;
            default:
                throw new NullPointerException("This Filter has no filter type set");
        }

        if (getKeywords().size() > 0) {
            for (String keyword : getKeywords()) {
                for (String compare : compareTo) {

                    Log.e("Ember", keyword + " " + compare + " " + String.valueOf(compare.toLowerCase().contains(keyword.toLowerCase())));
//                    if (keyword.toLowerCase().equals(compare.toLowerCase()))
                    if (compare.toLowerCase().contains(keyword.toLowerCase()))
                        return true;
                        //is = keyword.toLowerCase().equals(compare.toLowerCase());
                }
            }
        } else {
            return true;
        }

        return false;

    }

}
