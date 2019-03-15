import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovieListTest {

    private Movie m1;
    private Movie m2;
    private MovieList ml;


    @Before
    public void initialize(){
        m1 = new Movie("Movie 1");
        m2 = new Movie("Movie 2");
        ml = new MovieList();
        ml.add(m1);
        ml.add(m2);
    }

    @Test
    public void testLoad(){
        //TODO write the test for Movie loaded when loader.load("Space Jam" is indeed the right movie
        assertTrue(true);
    }

    @Test
    public void checkIMDBSort(){
        m1.setImdbRating(1.0);
        m2.setImdbRating(2.0);
        assertEquals(ml.getActiveList().get(0).getTitle(), "Movie 1");
        assertEquals(ml.getActiveList().get(1).getTitle(), "Movie 2");
    }
}
