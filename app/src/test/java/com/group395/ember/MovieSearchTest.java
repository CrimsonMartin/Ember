package com.group395.ember;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MovieSearchTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;
    private MovieSearch m = new MovieSearch();
    private int pageNumMoviesReturned;


    @Before
    public void initialize(){
        pageNumMoviesReturned = 6;
    }

    @Test
    public void searchByActor() throws InterruptedException{
        ArrayList<Movie> results = new ArrayList<>();
        String query = "Kevin Spacey";
        System.out.println("**By Actor**");
        long start = System.nanoTime();
        m.searchByActor(query);
        while(results.size() < pageNumMoviesReturned){
            results.add(m.results.take());
            System.out.println(results.get(results.size()-1));
        }
        long end = System.nanoTime();
        System.out.println("Number of Results: " + results);

        System.out.println("Full: "+(end-start)/(1000000*1000.0));
    }

    @Test
    public void search()throws InterruptedException{
        List<Movie> results = new ArrayList<>();
        String query = "star";
        System.out.println("** By Movie **");
        long start = System.nanoTime();
        m.searchFull(query);
        while(results.size() < pageNumMoviesReturned){
            results.add(m.results.take());
        }
        long end = System.nanoTime();
        System.out.println("First Page: "+ (end-start)/(1000000*1000.0));
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

    @Test
    public void url() throws InterruptedException{
        m.searchFull("Remember the");
        Thread.sleep(2000);

        for (Movie m : m.results){
            System.out.println(m.toString());
        }

    }
}
