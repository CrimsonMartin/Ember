package com.group395.ember;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class MovieLoaderTest {

    private MovieLoader ml;
    private Movie goalSpaceJam;

    @Before
    public void initialize(){
        ml = new MovieLoader();
        goalSpaceJam = new Movie("Space Jam");
        goalSpaceJam.setYear(1996);
        goalSpaceJam.setReleased("15 Nov 1996");
        goalSpaceJam.setDirector("Joe Pytka");
        goalSpaceJam.setWriter(Arrays.asList("Leo Benvenuti, Steve Rudnick, Timothy Harris, Herschel Weingrod".split(",")));
        goalSpaceJam.setActors(Arrays.asList("Michael Jordan, Wayne Knight, Theresa Randle, Manner Washington".split(",")));
        goalSpaceJam.setProduction("Warner Home Video");
    }


    @Test
    public void testUrlConlstructionTest(){
        String title = ml.omdbUrlFromTitle("Space Jam");
        assertEquals("http://www.omdbapi.com/?apikey=33d1a530&t=Space+Jam&plot=full", title);
    }

    @Test
    public void testLoadingMovie(){
        Movie actual = ml.loadMoviebyTitle("Space Jam");
        assertEquals(goalSpaceJam, actual);
    }

    @Test
    public void loadPlatformsfromWeb(){
        ml.loadPlatforms(goalSpaceJam);
        assertEquals("[Rakuten TV, TalkTalk TV Store, Sky Family HD (United Kingdom), Sky Cinema Family, iTunes, Amazon Prime, Now TV]", goalSpaceJam.getPlatforms().toString());
    }



}
