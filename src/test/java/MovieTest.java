import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class MovieTest {

    //{"Title":"Space Jam",
    // "Year":"1996",
    // "Rated":"PG",
    // "Released":"15 Nov 1996",
    // "Runtime":"88 min",
    // "Genre":"Animation, Adventure, Comedy, Family, Fantasy, Sci-Fi, Sport",
    // "Director":"Joe Pytka",
    // "Writer":"Leo Benvenuti, Steve Rudnick, Timothy Harris, Herschel Weingrod",
    // "Actors":"Michael Jordan, Wayne Knight, Theresa Randle, Manner Washington",
    // "Plot":"Swackhammer, owner of the amusement park planet Moron Mountain is
    //      desperate get new attractions and he decides that the Looney Tune
    //      characters would be perfect. He sends his diminutive underlings to get
    //      them to him, whether Bugs Bunny & Co. want to go or not. Well armed
    //      for their size, Bugs Bunny is forced to trick them into agreeing to a
    //      competition to determine their freedom. Taking advantage of their puny
    //      and stubby legged foes, the gang selects basketball for the surest
    //      chance of winning. However, the Nerdlucks turn the tables and steal the
    //      talents of leading professional basketball stars to become massive
    //      basketball bruisers known as the Monstars. In desperation, Bugs Bunny
    //      calls on the aid of Micheal Jordan, the Babe Ruth of Basketball, to
    //      help them have a chance at winning their freedom.",
    // "Language":"English",
    // "Country":"USA",
    // "Awards":"5 wins & 7 nominations.",
    // "Poster":"https://m.media-amazon.com/images/M/MV5BMDgyZTI2YmYtZmI4ZC00MzE0LWIxZWYtMWRlZWYxNjliNTJjXkEyXkFqcGdeQXVyNjY5NDU4NzI@._V1_SX300.jpg",
    // "Ratings":[{"Source":"Internet Movie Database","Value":"6.4/10"},{"Source":"Rotten Tomatoes","Value":"38%"},{"Source":"Metacritic","Value":"59/100"}],
    // "Metascore":"59",
    // "imdbRating":"6.4",
    // "imdbVotes":"138,962",
    // "imdbID":"tt0117705",
    // "Type":"movie",
    // "DVD":"27 Aug 1997",
    // "BoxOffice":"N/A",
    // "Production":"Warner Home Video",
    // "Website":"N/A",
    // "Response":"True"}
    /*
    private String Title;
    private Integer Year;
    private Date Released;
    private String Runtime;
    private List<String> Genre;
    private String Director;
    private List<String> Writer;
    private List<String> Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private String Ratings;
    private Integer Metascore;
    private Integer imdbRating;
    private String imdbID;
    private String Type;
    private String DVD;
    private String BoxOffice;
    private String Production;
    private String Website;
    private String Response;
    */

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
