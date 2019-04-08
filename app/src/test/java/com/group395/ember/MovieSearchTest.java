package com.group395.ember;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MovieSearchTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;


    @Before
    public void initialize(){
    }

    @Test
    public void searchByActor() throws InterruptedException{
        String query = "Tom Cruise";
        System.out.println("**By Actor**");
        long start = System.nanoTime();
        MovieSearch.searchByActor(query);
        long end = System.nanoTime();
        System.out.println("Partial: "+ (end-start)/(1000000*1000.0));
        start = System.nanoTime();
        List<Movie> res = MovieSearch.searchByActorFull(query);
        System.out.println("Number of Results: "+ res.size());
        end = System.nanoTime();
        System.out.println("Full: "+(end-start)/(1000000*1000.0));
    }

    @Test
    public void search()throws InterruptedException{
        String query = "Blues Brothers";
        System.out.println("** By Movie **");
        long start = System.nanoTime();
        MovieSearch.searchFirstPage(query);
        long end = System.nanoTime();
        System.out.println("First Page: "+ (end-start)/(1000000*1000.0));
        start = System.nanoTime();
        List<Movie> res = MovieSearch.searchByActorFull(query);
        System.out.println("Number of Results: " + res.size());
        end = System.nanoTime();
        System.out.println("Full: "+(end-start)/(1000000*1000.0));
       // assertTrue(MovieSearch.search("Return of the").getResponse());
       // assertTrue(MovieSearch.search("Titan").getResponse());
      //  assertFalse(MovieSearch.search("the").getResponse());
    }
}
