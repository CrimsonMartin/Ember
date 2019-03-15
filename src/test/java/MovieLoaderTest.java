import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MovieLoaderTest {

    private MovieLoader ml;

    @Before
    public void init(){
        ml = new MovieLoader();
    }


    @Test
    public void UrlConlstructionTest(){
        String title = ml.searchUrlFromTitle("Space Jam");
        assertEquals("http://www.omdbapi.com/?apikey=33d1a530&t=Space+Jam&plot=full", title);
    }

    @Test
    public void checkLoadingMovie(){
        System.out.println(ml.loadMoviebyTitle("Space Jam").toString());
    }


}
