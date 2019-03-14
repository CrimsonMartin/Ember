import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MovieTest {

    private Movie m1;
    private Movie m2;


    @Before
    public void initialize(){
        m1 = new Movie("Movie 1");
        m2 = new Movie("Space Jam");

    }

    @Test
    public void testLoad(){
        //TODO write the test for Movie loaded when loader.load("Space Jam" is indeed the right movie
        assertTrue(true);
    }

}
