package com.group395.ember;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Movie {

    private String Title;
    private String Year;
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
    private List<String> Platforms = new ArrayList<>();

    public String getTitle() { return Title; }
    public void setTitle(String title) { Title = title; }

    public String getYear() { return Year; }
    public void setYear(String year) { Year = year; }

    public String getReleased() { return Released; }
    public void setReleased(String released) { Released = released; }

    public String getRuntime() { return Runtime; }
    public void setRuntime(String runtime) { Runtime = runtime; }

    public List<String> getGenre() { return Genre; }
    public void setGenre(List<String> genre) { Genre = genre; }

    public String getDirector() { return Director; }
    public void setDirector(String director) { Director = director; }

    public List<String> getWriter() { return Writer; }
    public void setWriter(List<String> writer) { Writer = writer; }

    public List<String> getActors() { return Actors; }
    public void setActors(List<String> actors) { Actors = actors; }

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

    public List<String> getPlatforms() { return Platforms; }
    public void addPlatforms(List<location> platforms){
        for (location l: platforms) {
            addPlatform(l);
        }
    }

    public void addPlatform(location l){ Platforms.add(l.display_name);}

    public void clearPlatforms(){ Platforms.clear(); }

    /**
     * Creates a new Movie object with the given title
     * @param title the title of the movie
     */
    Movie(String title){
        setTitle(title);
    }

    //this version of the constructor is for loading full movies
    protected Movie(jsonMovie jmv){

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

    //this version of the constructor is for loading simple movies from search results
    protected Movie(simpleJsonMovie jmv) {
        setTitle(jmv.Title);
        setYear(jmv.Year);
        setReleased(jmv.Released);
        setImdbID(jmv.imdbID);
        setType(jmv.Type);
        setPoster(jmv.Poster);
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

        addPlatforms(platforms);

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
        Double imdbRating;
        String imdbID;
        String Type;
        String DVD;
        String BoxOffice;
        String Production;
        String Website;
        String Response;
    }

    //simpleJsonMovie contains only the information included in the search results
    class simpleJsonMovie {
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
        List<result> results;
    }

    private class result{
        private String name;
        private Integer weight;
        List<location> locations;
    }

    private class location{
        private String name;
        private String icon;
        String display_name;
        private String url;
    }


}