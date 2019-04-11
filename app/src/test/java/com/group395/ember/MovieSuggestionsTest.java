package com.group395.ember;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MovieSuggestionsTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;


    @Before
    public void initialize(){
    }

    @Test
    public void getSuggestions(){
        Movie m = MovieSearch.searchFirstPage("John Wick").get(0);
        System.out.println(m);
        System.out.println(MovieSuggestions.getSuggestions(m));
       // assertTrue(MovieSearch.search("Return of the").getResponse());
       // assertTrue(MovieSearch.search("Titan").getResponse());
      //  assertFalse(MovieSearch.search("the").getResponse());
    }
}
