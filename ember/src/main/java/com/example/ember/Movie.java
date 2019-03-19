import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


 class Movie {

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

    String getTitle() {
        return Title;
    }

     void setTitle(String title) {
        Title = title;
    }

     Integer getYear() {
        return Year;
    }

     void setYear(Integer year) {
        Year = year;
    }

     String getReleased() {
        return Released;
    }

     void setReleased(String released) {
        Released = released;
    }

     String getRuntime() {
        return Runtime;
    }

     void setRuntime(String runtime) {
        Runtime = runtime;
    }

     List<String> getGenre() {
        return Arrays.asList(Genre.split(","));
    }

     void setGenre(List<String> genres) { Genre = String.join(",", genres);}

     String getDirector() {
        return Director;
    }

     void setDirector(String director) { Director = director; }

     List<String> getWriter() {
        return Arrays.asList(Writer.split(","));
    }

     void setWriter(List<String> writer) { Writer = String.join(",", writer); }

     List<String> getActors() {
        return Arrays.asList(Actors.split(","));
    }

     void setActors(List<String> actors) {
        Actors = String.join(",", actors);
    }

     String getPlot() {
        return Plot;
    }

     void setPlot(String plot) {
        Plot = plot;
    }

     String getLanguage() {
        return Language;
    }

     void setLanguage(String language) {
        Language = language;
    }

     String getCountry() {
        return Country;
    }

     void setCountry(String country) {
        Country = country;
    }

     String getAwards() {
        return Awards;
    }

     void setAwards(String awards) {
        Awards = awards;
    }

     String getPoster() {
        return Poster;
    }

     void setPoster(String poster) {
        Poster = poster;
    }

     Integer getMetascore() {
        return Metascore;
    }

     void setMetascore(Integer metascore) {
        Metascore = metascore;
    }

     Double getImdbRating() {
        return imdbRating;
    }

     void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

     String getImdbID() {
        return imdbID;
    }

     void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

     String getType() {
        return Type;
    }

     void setType(String type) {
        Type = type;
    }

     String getDVD() {
        return DVD;
    }

     void setDVD(String DVD) {
        this.DVD = DVD;
    }

     String getBoxOffice() {
        return BoxOffice;
    }

     void setBoxOffice(String boxOffice) {
        BoxOffice = boxOffice;
    }

     String getProduction() {
        return Production;
    }

     void setProduction(String production) {
        Production = production;
    }

     String getWebsite() {
        return Website;
    }

     void setWebsite(String website) {
        Website = website;
    }

     String getResponse() {
        return Response;
    }

     void setResponse(String response) {
        Response = response;
    }

     List<String> getPlatforms() {
        return platforms;
    }

     void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    private List <String> platforms;

     Movie(String title) {
        setTitle(title);
    }

     Movie(){}

     static Movie parseFromJson(BufferedReader reader){
        Gson gson = new Gson();
        return gson.fromJson(reader, Movie.class);
    }

     static Movie parseFromJson(String str){
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