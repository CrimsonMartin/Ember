package com.group395.ember;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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
    public void loadPlatformsfromWeb() {

        ml.loadPlatforms(goalSpaceJam);

        LinkedHashSet<String> platforms = new LinkedHashSet<String>() {{
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
                                .ofType(Set.class)
                                .andWithName("Platforms"),
                        CoreMatchers.<Set>equalTo(platforms));
    }

    @Test
    public void loadSchindlersList() throws InterruptedException{
        String title = "Schindler's List";
        ml.loadMoviebyTitle(title);
        Movie returned = ml.LoadedMovies.take();
        assertThat(returned.getTitle(), equalTo(title));
    }

    @Test
    public void loadGiberish() throws InterruptedException{
        String title = "ASDFASDFAVNFERKGJDFNGSDKF";
        ml.loadMoviebyTitle(title);
        Movie returned = ml.LoadedMovies.take();
        assertThat(returned.isInvalid(), is(true));
    }

    @Test
    public void loadMovieInPlace() throws Exception{
        Movie m = new Movie("Space Jam");
        Future<Movie> returned = ml.loadMovie(m);
        Movie returnedmovie = returned.get();
        assertThat(returnedmovie.getYear(), is(equalTo(1996)));
    }

    @Ignore
    public void testOrderofLoading() throws Exception{
        List<Movie> m1 = new ArrayList<Movie>(){{
            add(new Movie("Space Jam"));
            add(new Movie("Remember the titans"));
            add(new Movie("Saving private Ryan"));
            add(new Movie("Silver linings Playbook"));
        }};

        List<Future<Movie>> returned = ml.loadMovies(m1);

        Movie spacejam = returned.get(0).get();
        Movie rmember = returned.get(1).get();
        Movie savingprivate = returned.get(2).get();
        Movie slp = returned.get(3).get();

        assertThat(spacejam.getYear(), is(equalTo(1996)));
        assertThat(rmember.getYear(), is(equalTo(2000)));
        assertThat(savingprivate.getYear(), is(equalTo(1998)));
        assertThat(slp.getYear(), is(equalTo(2012)));

    }


}
