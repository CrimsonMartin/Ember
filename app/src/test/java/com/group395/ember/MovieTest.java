package com.group395.ember;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MovieTest {

    private String SpaceJamJson = "{\"Title\":\"Space Jam\",\"Year\":\"1996\",\"Rated\":\"PG\",\"Released\":\"15 Nov 1996\",\"Runtime\":\"88 min\",\"Genre\":\"Animation, Adventure, Comedy, Family, Fantasy, Sci-Fi, Sport\",\"Director\":\"Joe Pytka\",\"Writer\":\"Leo Benvenuti, Steve Rudnick, Timothy Harris, Herschel Weingrod\",\"Actors\":\"Michael Jordan, Wayne Knight, Theresa Randle, Manner Washington\",\"Plot\":\"Swackhammer, owner of the amusement park planet Moron Mountain is desperate get new attractions and he decides that the Looney Tune characters would be perfect. He sends his diminutive underlings to get them to him, whether Bugs Bunny & Co. want to go or not. Well armed for their size, Bugs Bunny is forced to trick them into agreeing to a competition to determine their freedom. Taking advantage of their puny and stubby legged foes, the gang selects basketball for the surest chance of winning. However, the Nerdlucks turn the tables and steal the talents of leading professional basketball stars to become massive basketball bruisers known as the Monstars. In desperation, Bugs Bunny calls on the aid of Micheal Jordan, the Babe Ruth of Basketball, to help them have a chance at winning their freedom.\",\"Language\":\"English\",\"Country\":\"USA\",\"Awards\":\"5 wins & 7 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMDgyZTI2YmYtZmI4ZC00MzE0LWIxZWYtMWRlZWYxNjliNTJjXkEyXkFqcGdeQXVyNjY5NDU4NzI@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"6.4/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"38%\"},{\"Source\":\"Metacritic\",\"Value\":\"59/100\"}],\"Metascore\":\"59\",\"imdbRating\":\"6.4\",\"imdbVotes\":\"138,962\",\"imdbID\":\"tt0117705\",\"Type\":\"movie\",\"DVD\":\"27 Aug 1997\",\"BoxOffice\":\"N/A\",\"Production\":\"Warner Home Video\",\"Website\":\"N/A\",\"Response\":\"True\"}";
    private String platformJson = "{\"status_code\":200,\"variant\":\"utelly\",\"term\":\"space jam\",\"updated\":\"2019-03-19T04:04:25+0000\",\"results\":[{\"name\":\"Space Jam\",\"weight\":399,\"locations\":[{\"name\":\"WuakiTV\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/WuakiTV.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"56c6edcba54d7559fe5028e1\",\"display_name\":\"Rakuten TV\",\"url\":\"https://uk.wuaki.tv/movies/space-jam\"},{\"name\":\"BlinkBox\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/BlinkBox.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"5270b96ff0ca9f2a3c17b4f1\",\"display_name\":\"TalkTalk TV Store\",\"url\":\"https://www.talktalktvstore.co.uk/movies/space-jam-(734)\"},{\"name\":\"Sky Family HD (United Kingdom)\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/live_tv_square/55c20fbeebb7f94c833a91b9.jpg?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"55c20fbeebb7f94c833a91b9\",\"display_name\":\"Sky Family HD (United Kingdom)\",\"url\":null},{\"name\":\"Sky Cinema Family\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/live_tv_square/50d352abf0ca9f579800038d.jpg?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"50d352abf0ca9f579800038d\",\"display_name\":\"Sky Cinema Family\",\"url\":null},{\"name\":\"ITunes\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/ITunes.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"524b8c30f0ca9f60fe406025\",\"display_name\":\"iTunes\",\"url\":\"https://itunes.apple.com/gb/movie/space-jam/id283825211?uo=5&at=1l3v7yf\"},{\"name\":\"Amazon\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/Amazon.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"531072c6f0ca9f1c439eb6cf\",\"display_name\":\"Amazon Prime\",\"url\":\"http://www.amazon.co.uk/gp/product/B00ET01RQS?tag=utellycom00-21\"},{\"name\":\"NowTV\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/NowTV.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"5270b96ff0ca9f2a3c17b4ee\",\"display_name\":\"Now TV\",\"url\":\"http://watch.nowtv.com/watch-movies/show-title/3cb70694e932d510VgnVCM1000000b43150a____\"}],\"id\":\"57633b7eebb7f956559d7939\",\"picture\":\"https://utellyassets2-10.imgix.net/2/Open/Warner_Home_Video_892/Program/5376/_2by1/KA.jpg?fit=crop&auto=compress&crop=faces,top\"}]}\n";

    private Movie m1;
    private Movie m2;
    private Movie spaceJamMovie;
    private Movie goalSpaceJam;

    @Before
    public void initialize(){
        m1 = new Movie("Movie 1");
        m2 = new Movie("Space Jam");
        spaceJamMovie = Movie.parseFromJson(SpaceJamJson);

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
    public void testmovieParseFromJson(){
        assertEquals(goalSpaceJam, spaceJamMovie);
    }

    @Test
    public void loadPlatformsfromString(){
        MovieLoader ml = new MovieLoader();
        ml.loadPlatforms(goalSpaceJam, platformJson);
        assertEquals("[Rakuten TV, TalkTalk TV Store, Sky Family HD (United Kingdom), Sky Cinema Family, iTunes, Amazon Prime, Now TV]", goalSpaceJam.getPlatforms().toString());
    }
}
