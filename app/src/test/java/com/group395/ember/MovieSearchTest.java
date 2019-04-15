package com.group395.ember;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MovieSearchTest {

    private Movie m1;
    private Movie m2;
    private MovieSearch m;
    private int pageNumMoviesReturned;


    @Before
    public void initialize(){
        pageNumMoviesReturned = 6;
    }

    @Ignore
    public void searchByActor() throws InterruptedException{
        ArrayList<Movie> results = new ArrayList<>();
        String query = "Kevin Spacey";
        m.searchByActor(query);
        while(results.size() < pageNumMoviesReturned){
            results.add(m.results.take());
        }
        assertEquals(results.size(), pageNumMoviesReturned);
    }

    @Ignore
    public void search()throws InterruptedException{
        List<Movie> results = new ArrayList<>();
        String query = "star";
        long start = System.nanoTime();
        m.searchFull(query);
        while(results.size() < pageNumMoviesReturned){
            results.add(m.results.take());
        }
        long end = System.nanoTime();
    }

    @Test
    public void testing()throws InterruptedException{
        String query = "Remember the";
        System.out.println("** By Movie **");
        long start = System.nanoTime();
        //MovieSearch.searchFirstPage(query);
        long end = System.nanoTime();
        System.out.println("First Page: "+ (end-start)/(1000000*1000.0));
        start = System.nanoTime();
        //List<Movie> res = MovieSearch.searchFull(query);
        //System.out.println("Number of Results: " + res.size());
        //System.out.println("Results: " + res.toString());
        end = System.nanoTime();
        System.out.println("Full: "+(end-start)/(1000000*1000.0));
    }

    @Ignore
    public void url() throws InterruptedException{
        m.searchFull("Remember the");
        Thread.sleep(2000);

        for (Movie m : m.results){
            System.out.println(m.toString());
        }

    }
}
