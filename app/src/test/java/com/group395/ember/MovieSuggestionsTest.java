package com.group395.ember;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MovieSuggestionsTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;
    private  MovieSuggestions ms;


    @Before
    public void initialize(){
        ms = new MovieSuggestions();
    }

    @Test
    public void getSuggestions(){
        MovieSearch search = new MovieSearch();
        Movie m = search.searchFirstPage("John Wick").get(0);
        System.out.println(m);
        System.out.println(ms.getSuggestions(m));
       // assertTrue(MovieSearch.search("Return of the").getResponse());
       // assertTrue(MovieSearch.search("Titan").getResponse());
      //  assertFalse(MovieSearch.search("the").getResponse());
    }
}
