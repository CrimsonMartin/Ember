package com.group395.ember;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
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
    public void testLoadingMovie() throws InterruptedException{
        ml.loadMoviebyTitle("Space Jam");
        assertEquals(goalSpaceJam, ml.LoadedMovies.take());

    }

    @Test
    public void testLoadingManyMovies() throws InterruptedException{
        ml.loadMoviebyTitle(new ArrayList<String>(){{
            add("Space Jam");
            add("Remember the titans");
            add("Saving private Ryan");
            add("Silver linings Playbook");
        }});
        try {
            Set<String> returned = new LinkedHashSet<String>();
            returned.add(ml.LoadedMovies.take().getTitle());
            returned.add(ml.LoadedMovies.take().getTitle());
            returned.add(ml.LoadedMovies.take().getTitle());
            returned.add(ml.LoadedMovies.take().getTitle());
            assertTrue(returned.contains("Space Jam"));
            assertTrue(returned.contains("Remember the Titans"));
            assertTrue(returned.contains("Saving Private Ryan"));
            assertTrue(returned.contains("Silver Linings Playbook"));
        }catch(InterruptedException e){
            fail();
        }

    }

    @Test
    public void loadPlatformsfromWeb(){

        ml.loadPlatforms(goalSpaceJam);

        ArrayList<String> platforms = new ArrayList<String>(){{
            add("Rakuten TV");
            add("TalkTalk TV Store");
            add("Sky Family HD (United Kingdom)");
            add("Sky Cinema Family");
            add("iTunes");
            add("Amazon Prime");
            add("Now TV");
        }};

        await().atMost(10, TimeUnit.SECONDS)
                .until(fieldIn(goalSpaceJam)
                                .ofType(List.class)
                                .andWithName("Platforms"),
                        CoreMatchers.<List>equalTo(platforms));
}


}
