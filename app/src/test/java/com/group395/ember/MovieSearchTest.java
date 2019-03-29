package com.group395.ember;

import org.junit.Before;
import org.junit.Test;

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
    public void search(){
        assertTrue(MovieSearch.search("Return of the").getResponse());
        assertTrue(MovieSearch.search("Titan").getResponse());
        assertFalse(MovieSearch.search("the").getResponse());
    }
}
