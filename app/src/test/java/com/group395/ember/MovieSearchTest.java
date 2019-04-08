package com.group395.ember;

import org.junit.Before;
import org.junit.Ignore;

public class MovieSearchTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;


    @Before
    public void initialize(){
    }

    @Ignore
    public void search(){
        String query = "end of";
        long start = System.nanoTime();
        MovieSearch.searchFirstPage(query);
        long end = System.nanoTime();
        System.out.println("First Page: "+ (end-start)/(1000000*1000.0));
        start = System.nanoTime();
        System.out.println("Number of Results: "+ MovieSearch.searchFull(query).size());
        end = System.nanoTime();
        System.out.println("Full: "+(end-start)/(1000000*1000.0));
       // assertTrue(MovieSearch.search("Return of the").getResponse());
       // assertTrue(MovieSearch.search("Titan").getResponse());
      //  assertFalse(MovieSearch.search("the").getResponse());
    }
}
