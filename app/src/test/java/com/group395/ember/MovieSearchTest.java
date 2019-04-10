package com.group395.ember;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MovieSearchTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;
    private MovieSearch m;


    @Before
    public void initialize(){
        m = new MovieSearch();
    }

    @Test
    public void searchByActor() throws InterruptedException{
        String query = "Tom Cruise";
        System.out.println("**By Actor**");
        long start = System.nanoTime();
        System.out.println(m.searchByActor(query));
        long end = System.nanoTime();
        System.out.println("Partial: "+ (end-start)/(1000000*1000.0));
        start = System.nanoTime();
        List<Movie> res = m.searchByActorFull(query);
        System.out.println("Number of Results: "+ res);
        end = System.nanoTime();
        System.out.println("Full: "+(end-start)/(1000000*1000.0));
    }

    @Test
    public void search()throws InterruptedException{
        String query = "Blues Brothers";
        System.out.println("** By Movie **");
        long start = System.nanoTime();
        m.searchFirstPage(query);
        long end = System.nanoTime();
        System.out.println("First Page: "+ (end-start)/(1000000*1000.0));
        start = System.nanoTime();
        List<Movie> res = m.searchFull(query);
        System.out.println("Number of Results: " + res.size());
        end = System.nanoTime();
        System.out.println("Full: "+(end-start)/(1000000*1000.0));
       // assertTrue(MovieSearch.search("Return of the").getResponse());
       // assertTrue(MovieSearch.search("Titan").getResponse());
      //  assertFalse(MovieSearch.search("the").getResponse());
    }

    @Test
    public void testing()throws InterruptedException{
        String query = "Remember the";
        System.out.println("** By Movie **");
        long start = System.nanoTime();
        m.searchFirstPage(query);
        long end = System.nanoTime();
        System.out.println("First Page: "+ (end-start)/(1000000*1000.0));
        start = System.nanoTime();
        List<Movie> res = m.searchFull(query);
        System.out.println("Number of Results: " + res.size());
        System.out.println("Results: " + res.toString());
        end = System.nanoTime();
        System.out.println("Full: "+(end-start)/(1000000*1000.0));
    }
}
