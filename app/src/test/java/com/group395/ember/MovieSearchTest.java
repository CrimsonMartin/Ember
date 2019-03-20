package com.group395.ember;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovieSearchTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;


    @Before
    public void initialize(){
        m1 = new Movie("Superbad");
        m2 = new Movie("Batman vs. Superman");
        ml = new MovieList();
        ml.add(m1);
        ml.add(m2);
    }

    @Test
    public void testParser(){
        String[] withQuotes = {"saving private ryan"};
        String[] withoutQuotes = {"saving", "private", "ryan"};
        assertArrayEquals(withQuotes, MovieSearch.keyPhraseParser("\"Saving Private Ryan\"").toArray());
        assertArrayEquals(withoutQuotes, MovieSearch.keyPhraseParser("SAVING PRIVATE RYAN").toArray());
}

}
