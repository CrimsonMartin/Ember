package com.group395.ember;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class MovieLoaderTest {

    private MovieLoader ml;
    private Movie goalSpaceJam;

    private String platformJson = "{\"status_code\":200,\"variant\":\"utelly\",\"term\":\"space jam\",\"updated\":\"2019-03-19T04:04:25+0000\",\"results\":[{\"name\":\"Space Jam\",\"weight\":399,\"locations\":[{\"name\":\"WuakiTV\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/WuakiTV.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"56c6edcba54d7559fe5028e1\",\"display_name\":\"Rakuten TV\",\"url\":\"https://uk.wuaki.tv/movies/space-jam\"},{\"name\":\"BlinkBox\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/BlinkBox.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"5270b96ff0ca9f2a3c17b4f1\",\"display_name\":\"TalkTalk TV Store\",\"url\":\"https://www.talktalktvstore.co.uk/movies/space-jam-(734)\"},{\"name\":\"Sky Family HD (United Kingdom)\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/live_tv_square/55c20fbeebb7f94c833a91b9.jpg?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"55c20fbeebb7f94c833a91b9\",\"display_name\":\"Sky Family HD (United Kingdom)\",\"url\":null},{\"name\":\"Sky Cinema Family\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/live_tv_square/50d352abf0ca9f579800038d.jpg?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"50d352abf0ca9f579800038d\",\"display_name\":\"Sky Cinema Family\",\"url\":null},{\"name\":\"ITunes\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/ITunes.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"524b8c30f0ca9f60fe406025\",\"display_name\":\"iTunes\",\"url\":\"https://itunes.apple.com/gb/movie/space-jam/id283825211?uo=5&at=1l3v7yf\"},{\"name\":\"Amazon\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/Amazon.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"531072c6f0ca9f1c439eb6cf\",\"display_name\":\"Amazon Prime\",\"url\":\"http://www.amazon.co.uk/gp/product/B00ET01RQS?tag=utellycom00-21\"},{\"name\":\"NowTV\",\"icon\":\"https://utellyassets7.imgix.net/locations_icons/utelly/black_new/NowTV.png?auto=compress&app_version=f901fd62-83e7-4a67-90aa-e468871982f2_2019-03-19&w=92\",\"id\":\"5270b96ff0ca9f2a3c17b4ee\",\"display_name\":\"Now TV\",\"url\":\"http://watch.nowtv.com/watch-movies/show-title/3cb70694e932d510VgnVCM1000000b43150a____\"}],\"id\":\"57633b7eebb7f956559d7939\",\"picture\":\"https://utellyassets2-10.imgix.net/2/Open/Warner_Home_Video_892/Program/5376/_2by1/KA.jpg?fit=crop&auto=compress&crop=faces,top\"}]}\n";

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
    public void testUrlConlstructionTest(){
        String title = ml.omdbUrlFromTitle("Space Jam");
        assertEquals("http://www.omdbapi.com/?apikey=33d1a530&t=Space+Jam&plot=full", title);
    }

    @Test
    public void testLoadingMovie(){
        Movie actual = ml.loadMoviebyTitle("Space Jam");
        assertEquals(goalSpaceJam, actual);
    }

    @Test
    public void loadPlatformsfromString(){
        ml.loadPlatforms(goalSpaceJam, platformJson);
        //System.out.println(goalSpaceJam.getPlatforms());
        assertEquals("[Rakuten TV, TalkTalk TV Store, Sky Family HD (United Kingdom), Sky Cinema Family, iTunes, Amazon Prime, Now TV]", goalSpaceJam.getPlatforms().toString());
    }

    @Test
    public void loadPlatformsfromWeb(){
        ml.loadPlatforms(goalSpaceJam);
        assertEquals("[Rakuten TV, TalkTalk TV Store, Sky Family HD (United Kingdom), Sky Cinema Family, iTunes, Amazon Prime, Now TV]", goalSpaceJam.getPlatforms().toString());
    }



}
