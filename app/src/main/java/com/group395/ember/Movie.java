package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.isNull;

public class Movie {

    private String Title;
    private String Year = null;
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
    private String Metascore;
    private Double imdbRating;
    private String imdbID;
    private String Type;
    private String DVD;
    private String BoxOffice;
    private String Production;
    private String Website;
    private String Response;
    private Integer tmdbID;

    public Set<String> Platforms = new LinkedHashSet<>();

    //Doubles and Ints we end up using need parsed first, because many times the API passes "N/A"
    private Double rating;

    public Double getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Double releaseYear) {
        this.releaseYear = releaseYear;
    }

    private Double releaseYear;

    public Integer getTmdbID() {
        return tmdbID;
    }

    public void setTmdbID(Integer tmdbID) {
        this.tmdbID = tmdbID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) { Title = title; }

    public Integer getYear() {
        try {
            Integer year = Integer.parseInt(Year);
            return year;
        } catch (Exception e) {
            return -1;
        }
    }

    public void setYear(Integer year) { Year = year.toString(); }

    public String getReleased() { return Released; }
    public void setReleased(String released) { Released = released; }

    public String getRuntime() { return Runtime; }
    public void setRuntime(String runtime) { Runtime = runtime; }

    public List<String> getGenre() { return Genre; }
    public void setGenre(List<String> genre) {
        Genre = new LinkedList<>();
        for (String s : genre){
            Genre.add(s.trim());
        }
    }

    public String getDirector() { return Director; }
    public void setDirector(String director) { Director = director; }

    public List<String> getWriter() { return Writer; }
    public void setWriter(List<String> writer) { Writer = writer; }

    public List<String> getActors() { return Actors; }
    public void setActors(List<String> actors) {
        Actors = new LinkedList<>();
        for (String s : actors){
            Actors.add(s.trim());
        }
    }

    public String getPlot() { return Plot; }
    public void setPlot(String plot) { Plot = plot; }

    public String getLanguage() { return Language; }
    public void setLanguage(String language) { Language = language; }

    public String getCountry() { return Country; }
    public void setCountry(String country) { Country = country; }

    public String getAwards() { return Awards; }
    public void setAwards(String awards) { Awards = awards; }

    public String getPoster() { return Poster; }
    public void setPoster(String poster) { Poster = poster; }

    public String getMetascore() { return Metascore; }
    public void setMetascore(String metascore) { Metascore = metascore; }

    public Double getImdbRating() { return imdbRating; }
    public void setImdbRating(Double imdbRating) { this.imdbRating = imdbRating; }

    public String getImdbID() { return imdbID; }
    public void setImdbID(String imdbID) { this.imdbID = imdbID; }

    public String getType() { return Type; }
    public void setType(String type) { Type = type; }

    public String getDVD() { return DVD; }
    public void setDVD(String DVD) { this.DVD = DVD; }

    public String getBoxOffice() { return BoxOffice; }
    public void setBoxOffice(String boxOffice) { BoxOffice = boxOffice; }

    public String getProduction() { return Production; }
    public void setProduction(String production) { Production = production; }

    public String getWebsite() { return Website; }
    public void setWebsite(String website) { Website = website; }

    public String getResponse() { return Response; }
    public void setResponse(String response) { Response = response; }

    public Double getRating() {return rating; }
    public void setRating(Double newRating) { rating = newRating; }

    public Set<String> getPlatforms() { return Platforms; }
    public void addPlatforms(List<Location> platforms){
        for (Location l: platforms) {
            addPlatform(l);
        }
    }

    /**
     * Add a platform to this movie.
     * @param l the Location of where this movie can be viewed.
     */
    void addPlatform(Location l){ Platforms.add(l.display_name);}

    /**
     * Remove all the current platforms from this movie.
     */
    void clearPlatforms(){ Platforms.clear(); }

    /**
     * Creates a new Movie object with the given title
     * @param title the title of the movie
     */
    Movie(String title){
        setTitle(title);
    }

    Movie(){ setTitle(null); }

    /**
     * Check that the loading of a movie was successfull or not.
     * @return if a Movie was successfully loaded or not, ie if this movie is "valid".
     */
    boolean isInvalid(){return isNull(getTitle()); }

    /**
     * Load a full movie.
     * @param jmv The Json response of a Movie
     */
    Movie(jsonMovie jmv){

        setTitle(jmv.Title);
        setReleased(jmv.Released);
        setRuntime(jmv.Runtime);
        if(jmv.Genre != null)
            setGenre(Arrays.asList(jmv.Genre.split(",")));
        if(jmv.Director != null)
            setDirector(jmv.Director);
        if(jmv.Writer != null)
            setWriter(Arrays.asList(jmv.Writer.split(",")));
        if(jmv.Actors != null)
            setActors(Arrays.asList(jmv.Actors.split(",")));
        if(jmv.Plot != null)
            setPlot(jmv.Plot);
        if(jmv.Language != null)
            setLanguage(jmv.Language);
        setCountry(jmv.Country);
        setAwards(jmv.Awards);
        setPoster(jmv.Poster);
        setMetascore(jmv.Metascore);
        setImdbID(jmv.imdbID);
        setType(jmv.Type);
        setDVD(jmv.DVD);
        setBoxOffice(jmv.BoxOffice);
        setProduction(jmv.Production);
        setWebsite(jmv.Website);
        setResponse(jmv.Response);

        try {
            if(jmv.imdbRating != null)
                setImdbRating(Double.parseDouble(jmv.imdbRating));
        }catch (NumberFormatException e){
            setImdbRating(null);
        }

        try {
            setYear(Integer.parseInt(jmv.Year));
        }catch (NumberFormatException e){
            setImdbRating(null);
        }
    }

    /**
     *  Load a Movie. This is only on for testing purposes, to minimiza api calls.
     * @param str the Json response from OMDb for the information about this Movie
     * @return Movie the movie that has information loaded
     */
    static Movie parseFromJson(String str){
        Gson gson = new Gson();
        jsonMovie jmv = gson.fromJson(str, jsonMovie.class);
        return new Movie(jmv);
    }

    static Movie parseFromReader(BufferedReader reader){
        Gson gson = new Gson();
        jsonMovie jmv = gson.fromJson(reader, jsonMovie.class);
        return new Movie(jmv);
    }

    /**
     * Add platforms for viewing to this movie
     * @param jsonPlatforms the Json String response from UTelli
     *                      the platforms to be added to this movie
     */
    void addPlatforms(String jsonPlatforms){

        clearPlatforms();
        Gson gson = new Gson ();
        jsonPlatformResponse platformResponse = gson.fromJson(jsonPlatforms, jsonPlatformResponse.class);

        List<Location> platforms;

        Result results = platformResponse.results.stream()
                .findFirst()
                .orElse(null);

        if (results != null) addPlatforms(results.locations);

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
                ", tmdbID='" + tmdbID + '\'' +
                ", Type='" + Type + '\'' +
                ", DVD='" + DVD + '\'' +
                ", BoxOffice='" + BoxOffice + '\'' +
                ", Production='" + Production + '\'' +
                ", Website='" + Website + '\'' +
                ", Response='" + Response + '\'' +
                ", Platforms=" + Platforms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;

        boolean titleEqual = movie.getTitle().toLowerCase().equals(this.getTitle().toLowerCase());

        String movieID = movie.getImdbID();
        String thisID = this.getImdbID();

        if (movieID == null && thisID != null) return false;
        if (movieID != null && thisID == null) return false;

        if(movieID == null && thisID == null){
            return titleEqual && movie.getYear().equals(this.getYear());
        }

        return (titleEqual
                && movieID.equals(thisID)
                && movie.getYear().equals(this.getYear()));

    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getTitle().toLowerCase(),
                getImdbID()
        );
    }

    //full jsonMovie class where it reads in all info on movie
    private class jsonMovie {
        String Title;
        String Year;
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
        String Metascore;
        String imdbRating;
        String imdbID;
        String Type;
        String DVD;
        String BoxOffice;
        String Production;
        String Website;
        String Response;
    }

    //simpleJsonMovie contains only the information included in the search results
    private class simpleJsonMovie {
        String Title;
        String Year;
        String Released;
        String Type;
        String imdbID;
        String Poster;
    }

    private class jsonPlatformResponse{
        private String status_code;
        private String variant;
        private String term;
        private String updated;
        List<Result> results;
    }

    private class Result{
        private String name;
        private Integer weight;
        List<Location> locations;
    }

    private class Location{
        private String name;
        private String icon;
        String display_name;
        private String url;
    }


}