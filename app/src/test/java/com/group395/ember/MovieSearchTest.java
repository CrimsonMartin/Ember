package com.group395.ember;

import org.junit.Before;
import org.junit.Test;

import java.util.Timer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MovieSearchTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;


    @Before
    public void initialize(){
    }

    @Test
    public void searchByActor(){
        String query = "Tom Cruise";
        System.out.println("**By Actor**");
        long start = System.nanoTime();
        MovieSearch.searchByActor(query);
        long end = System.nanoTime();
        System.out.println("Partial: "+ (end-start)/(1000000*1000.0));
        start = System.nanoTime();
        System.out.println("Number of Results: "+ MovieSearch.searchByActorFull(query));
        end = System.nanoTime();
        System.out.println("Full: "+(end-start)/(1000000*1000.0));
    }

    @Test
    public void search(){
        String query = "Blues Brothers";
        System.out.println("** By Movie **");
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
