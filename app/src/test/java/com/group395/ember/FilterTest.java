package com.group395.ember;


import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FilterTest {
    
    private Filter filter = new Filter(FilterType.ACTOR);
    private Filter filter2 = new Filter(FilterType.ACTOR);
    private Movie movie;
    private ArrayList<String> f = new ArrayList<>();
    private MovieSearch mov;


    @Test
    public void addTest() {
        for(int i = 0; i<=999; i++){
            f.add("Test");
        }
        filter.add("Test");
        //Test 1
        assertThat(filter.getKeywords().contains("Test"), is(true));

        for(int i = 1; i<=999; i++){
            filter.add("Test");
        }
        //Test 2
        assertThat(filter.getKeywords().containsAll(f), is(true));

        //Test 3
        filter2.add("");
        assertThat( filter2.getKeywords().contains(""), is(true));
    }

    /*
    @Ignore
    public void fitsFilterTest() throws InterruptedException{
        //Test 1
        ArrayList<Movie> m = mov.searchFirstPage("Space Jam");
        MovieLoader ml = new MovieLoader();
        ml.loadMoviebyTitle(m.stream().findFirst().orElse(null).getTitle());
        Movie result = ml.LoadedMovies.take();
        Filter f1 = new Filter(FilterType.GENRE);
        f1.add("Comedy");
        assertThat(f1.fitsFilter(result), is(true));
        //Test 2
        Filter f2 = new Filter(FilterType.GENRE);
        f2.add("Horror");
        assertFalse(f2.fitsFilter(result));
        //Test 3
        Filter f3 = new Filter(FilterType.DIRECTOR);
        f3.add("Joe Pytka");
        assertTrue(f3.fitsFilter(result));

    }
    */
}
