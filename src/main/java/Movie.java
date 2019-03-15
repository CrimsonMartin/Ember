import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private Integer Metascore;
    private Double imdbRating;
    private String imdbID;
    private String Type;
    private String DVD;
    private String BoxOffice;
    private String Production;
    private String Website;
    private String Response;

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

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public List<String> getGenre() {
        return Arrays.asList(Genre.split(","));
    }

    public void setGenre(List<String> genres) { Genre = genres.stream().collect(Collectors.joining(",")); }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) { Director = director; }

    public List<String> getWriter() {
        return Arrays.asList(Writer.split(","));
    }

    public void setWriter(List<String> writer) { Writer = writer.stream().collect(Collectors.joining(",")); }

    public List<String> getActors() {
        return Arrays.asList(Actors.split(","));
    }

    public void setActors(List<String> actors) {
        Actors = actors.stream().collect(Collectors.joining(","));
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

    public Integer getMetascore() {
        return Metascore;
    }

    public void setMetascore(Integer metascore) {
        Metascore = metascore;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
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

    private List <String> platforms;

    public Movie(String title) {
        setTitle(title);
    }

    public Movie(){}

    public static Movie parseFromJson(BufferedReader reader){
        Gson gson = new Gson();
        return gson.fromJson(reader, Movie.class);
    }

    public static Movie parseFromJson(String str){
        Gson gson = new Gson();
        return gson.fromJson(str, Movie.class);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "Title='" + Title + '\'' +
                ", Year=" + Year +
                ", Released=" + Released +
                ", Runtime='" + Runtime + '\'' +
                ", Genre=" + Genre +
                ", Director='" + Director + '\'' +
                ", Writer=" + Writer +
                ", Actors=" + Actors +
                ", Plot='" + Plot + '\'' +
                ", Language='" + Language + '\'' +
                ", Country='" + Country + '\'' +
                ", Awards='" + Awards + '\'' +
                ", Poster='" + Poster + '\'' +
                ", Metascore=" + Metascore +
                ", imdbRating=" + imdbRating +
                ", imdbID='" + imdbID + '\'' +
                ", Type='" + Type + '\'' +
                ", DVD='" + DVD + '\'' +
                ", BoxOffice='" + BoxOffice + '\'' +
                ", Production='" + Production + '\'' +
                ", Website='" + Website + '\'' +
                ", Response='" + Response + '\'' +
                ", platforms=" + platforms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getTitle(), movie.getTitle()) &&
                Objects.equals(getYear(), movie.getYear()) &&
                Objects.equals(getReleased(), movie.getReleased()) &&
                Objects.equals(getDirector(), movie.getDirector()) &&
                Objects.equals(getActors(), movie.getActors()) &&
                Objects.equals(getProduction(), movie.getProduction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getYear(), getReleased(), getDirector(), getActors(), getProduction());
    }



}