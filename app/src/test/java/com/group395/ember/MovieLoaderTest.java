package com.group395.ember;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.fieldIn;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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
        goalSpaceJam.setImdbID("tt0117705");
    }

    @Test
    public void testLoadingMovie() throws InterruptedException, ExecutionException{
        Future<Movie> future = ml.loadMovieByTitle("Space Jam");
        Movie returned = future.get();
        assertThat(returned, is(equalTo(goalSpaceJam)));

    }

    @Test
    public void testLoadingManyMovies() {
        List<String> titles = new ArrayList<String>(){{
            add("Space Jam");
            add("Remember the Titans");
            add("Saving Private Ryan");
            add("Silver Linings Playbook");
        }};

        try {
            List<Future<Movie>> futures = ml.loadMoviesByTitle(titles);

            for (Future<Movie> f : futures){
                Movie m = f.get();
                assertThat(titles.contains(m.getTitle()), is(true));
            }
        }catch(ExecutionException | InterruptedException e){
            fail();
        }

    }

    @Test
    public void loadPlatformsfromWeb() {

        goalSpaceJam.clearPlatforms();
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
    public void loadSchindlersList() throws InterruptedException, ExecutionException{
        String title = "Schindler's List";
        Future<Movie> future = ml.loadMovieByTitle(title);
        Movie returned = future.get();
        assertThat(returned.getTitle(), equalTo(title));
    }

    @Test
    public void loadGiberish() throws InterruptedException, ExecutionException{
        String title = "ASDFASDFAVNFERKGJDFNGSDKF";
        Future<Movie> future = ml.loadMovieByTitle(title);
        Movie returned = future.get();
        assertThat(returned.isInvalid(), is(true));
    }

    @Test
    public void loadMovieInPlace() throws Exception{
        Movie m = new Movie("Space Jam");
        Future<Movie> future = ml.loadMovie(m);
        Movie returned = future.get();
        assertThat(returned.getYear(), is(equalTo(1996)));
    }

    @Test
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

    @Test
    public void testMovieCachingSpeed() throws Exception{
        Movie spaceJam = new Movie("Space Jam");
        String savingPrivateRyan = "Saving Private Ryan";
        List<Future<Movie>> returned = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for(int i = 0; i < 10000; i++){
            returned.add(ml.loadMovie(spaceJam));
            returned.add(ml.loadMovieByTitle(savingPrivateRyan));
        }

        for(Future f : returned){
            f.get();
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);

        assertTrue(duration < 1000);

    }

    @Test
    public void testPlatformCachingSpeed() throws Exception{
        goalSpaceJam.clearPlatforms();
        goalSpaceJam.setPlatforms(ml.loadPlatforms(goalSpaceJam));
        assertEquals(goalSpaceJam.getPlatforms().toString(),
                "[Rakuten TV, TalkTalk TV Store, Sky Family HD (United Kingdom), Sky Cinema Family, iTunes, Amazon Prime, Now TV]");

    }


}
