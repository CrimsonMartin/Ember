package com.group395.ember;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


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
    public void testLoadingMovie(){
        ml.loadMoviebyTitle("Space Jam");
        try{
            assertEquals(goalSpaceJam, ml.loadedmovies.take());
        } catch (InterruptedException e){
            fail();
        }
    }

    @Test
    public void testLoadingManyMovies(){
        ml.loadMoviebyTitle(new ArrayList<String>(){{
            add("Space Jam");
            add("Remember the titans");
            add("Saving private Ryan");
            add("Silver linings Playbook");
        }});
        try {
            Set<String> returned = new LinkedHashSet<String>();
            returned.add(ml.loadedmovies.take().getTitle());
            returned.add(ml.loadedmovies.take().getTitle());
            returned.add(ml.loadedmovies.take().getTitle());
            returned.add(ml.loadedmovies.take().getTitle());
            assertTrue(returned.contains("Space Jam"));
            assertTrue(returned.contains("Remember the Titans"));
            assertTrue(returned.contains("Saving Private Ryan"));
            assertTrue(returned.contains("Silver Linings Playbook"));
        }catch(InterruptedException e){
            fail();
        }

    }

    @Ignore
    public void loadPlatformsfromWeb(){
        ml.loadPlatforms(goalSpaceJam);
        assertEquals("[Rakuten TV, TalkTalk TV Store, Sky Family HD (United Kingdom), Sky Cinema Family, iTunes, Amazon Prime, Now TV]", goalSpaceJam.getPlatforms().toString());
    }



}
