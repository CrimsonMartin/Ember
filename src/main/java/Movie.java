import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.io.*;


public class Movie {

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

    private List <String> platforms;

    public Movie(String title) {
        setTitle(title);
    }

    public Movie(){}

    public static Movie parseFromJson(BufferedReader reader){
        Movie m = new Movie();
        //TODO WRITE ME
        return m;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Integer getYear() {
        return Year;
    }

    public void setYear(Integer year) {
        Year = year;
    }

    public Date getReleased() {
        return Released;
    }

    public void setReleased(Date released) {
        Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public List<String> getGenre() {
        return Genre;
    }

    public void setGenre(List<String> genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public List<String> getWriter() {
        return Writer;
    }

    public void setWriter(List<String> writer) {
        Writer = writer;
    }

    public List<String> getActors() {
        return Actors;
    }

    public void setActors(List<String> actors) {
        Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        Awards = awards;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getRatings() {
        return Ratings;
    }

    public void setRatings(String ratings) {
        Ratings = ratings;
    }

    public Integer getMetascore() {
        return Metascore;
    }

    public void setMetascore(Integer metascore) {
        Metascore = metascore;
    }

    public Integer getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Integer imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDVD() {
        return DVD;
    }

    public void setDVD(String DVD) {
        this.DVD = DVD;
    }

    public String getBoxOffice() {
        return BoxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        BoxOffice = boxOffice;
    }

    public String getProduction() {
        return Production;
    }

    public void setProduction(String production) {
        Production = production;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }


}