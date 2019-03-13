import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MovieTest {

    private Movie m1;
    private Movie m2;


    @Before
    public void initialize(){
        m1 = new Movie();
        m2 = new Movie();

    }

    @Test
    public void testLoad(){
        //TODO write the test for Movie loaded when loader.load("Space Jam" is indeed the right movie
        assertTrue(true);
    }

    @Test
    public void checkIMDBSort(){
        m1.setImdbrating(1);
        m2.setImdbrating(2);
        assertEquals(MovieIMDBRating.compare(m1, m2) , 1);
    }

}
