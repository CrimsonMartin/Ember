package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Movie {

    private String Title;
    private Integer Year;
    private String Released;
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
    private Integer Metascore;
    private Double imdbRating;
    private String imdbID;
    private String Type;
    private String DVD;
    private String BoxOffice;
    private String Production;
    private String Website;
    private String Response;

    private List<String> Platforms = new ArrayList<>();

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
        return Genre;
    }

    void setGenre(List<String> genre) {
        Genre = genre;
    }

    String getDirector() {
        return Director;
    }

    void setDirector(String director) {
        Director = director;
    }

    List<String> getWriter() {
        return Writer;
    }

    void setWriter(List<String> writer) {
        Writer = writer;
    }

    List<String> getActors() {
        return Actors;
    }

    void setActors(List<String> actors) {
        Actors = actors;
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

    public List<String> getPlatforms() {
        return Platforms;
    }

    void addPlatforms(List<String> platforms){Platforms.addAll(platforms);}

    void addPlatform(location l){ Platforms.add(l.display_name);}

    void clearPlatforms(){
        Platforms.clear();
    }

    Movie(String title){
        setTitle(title);
    }

    Movie(jsonMovie jmv){

        setTitle(jmv.Title);
        setYear(jmv.Year);
        setReleased(jmv.Released);
        setRuntime(jmv.Runtime);
        setGenre(Arrays.asList(jmv.Genre.split(",")));
        setDirector(jmv.Director);
        setWriter(Arrays.asList(jmv.Writer.split(",")));
        setActors(Arrays.asList(jmv.Actors.split(",")));
        setPlot(jmv.Plot);
        setLanguage(jmv.Language);
        setCountry(jmv.Country);
        setAwards(jmv.Awards);
        setPoster(jmv.Poster);
        setMetascore(jmv.Metascore);
        setImdbRating(jmv.imdbRating);
        setImdbID(jmv.imdbID);
        setType(jmv.Type);
        setDVD(jmv.DVD);
        setBoxOffice(jmv.BoxOffice);
        setProduction(jmv.Production);
        setWebsite(jmv.Website);
        setResponse(jmv.Response);

    }

    static Movie parseFromJson(BufferedReader reader){
        Gson gson = new Gson();
        jsonMovie jmv = gson.fromJson(reader, jsonMovie.class);
        return new Movie(jmv);
    }

    static Movie parseFromJson(String str){
        Gson gson = new Gson();
        jsonMovie jmv = gson.fromJson(str, jsonMovie.class);
        return new Movie(jmv);
    }

    void addPlatforms(String jsonPlatforms){

        clearPlatforms();
        Gson gson = new Gson ();
        jsonPlatformResponse platformResponse = gson.fromJson(jsonPlatforms, jsonPlatformResponse.class);

        List<location> platforms = platformResponse.results.stream()
                .findFirst()
                .orElse(null)
                .locations;

        for (location l: platforms) {
           addPlatform(l);
        }

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
                ", platforms=" + Platforms +
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

    private class jsonMovie {
        String Title;
        Integer Year;
        String Released;
        String Runtime;
        String Genre;
        String Director;
        String Writer;
        String Actors;
        String Plot;
        String Language;
        String Country;
        String Awards;
        String Poster;
        Integer Metascore;
        Double imdbRating;
        String imdbID;
        String Type;
        String DVD;
        String BoxOffice;
        String Production;
        String Website;
        String Response;
    }

    private class jsonPlatformResponse{
        String status_code;
        String variant;
        String term;
        String updated;
        List<result> results;
    }

    private class result{
        String name;
        Integer weight;
        List<location> locations;
    }

    private class location{
        String name;
        String icon;
        String display_name;
        String url;
    }


}