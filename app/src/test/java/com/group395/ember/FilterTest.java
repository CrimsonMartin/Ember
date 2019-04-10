package com.group395.ember;


import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class FilterTest {
    
    private Filter filter;
    private Filter filter2;
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
        assertEquals("Test", filter.getKeywords().get(0));

        for(int i = 1; i<=999; i++){
            filter.add("Test");
        }
        //Test 2
        assertEquals(f, filter.getKeywords());

        //Test 3
        filter2.add("");
        assertEquals("", filter2.getKeywords().get(0));
    }
    @Test
    public void fitsFilterTest(){
        //Test 1
        ArrayList<Movie> m = new ArrayList<>();
        m = mov.searchFirstPage("Space Jam");
        Filter f1 = new Filter(FilterType.GENRE);
        f1.add("Comedy");
        assertEquals(f1.fitsFilter(m.get(0)), true);
        //Test 2
        Filter f2 = new Filter(FilterType.GENRE);
        f2.add("Horror");
        assertEquals(f2.fitsFilter(m.get(0)), false);
        //Test 3
        Filter f3 = new Filter(FilterType.DIRECTOR);
        f3.add("Joe Pytka");
        assertEquals(f3.fitsFilter(m.get(0)), true);

    }
}
