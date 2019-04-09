package com.group395.ember;

import org.junit.Before;
import org.junit.Ignore;

public class MovieSuggestionsTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;


    @Before
    public void initialize(){
    }

    @Ignore
    public void getSuggestions(){
        MovieSearch search = new MovieSearch();
        Movie m = search.searchFirstPage("Space Jam").get(0);
        System.out.println(MovieSuggestions.getSuggestions(m));
       // assertTrue(MovieSearch.search("Return of the").getResponse());
       // assertTrue(MovieSearch.search("Titan").getResponse());
      //  assertFalse(MovieSearch.search("the").getResponse());
    }
}
