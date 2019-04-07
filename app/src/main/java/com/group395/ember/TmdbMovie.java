package com.group395.ember;

import java.util.ArrayList;

public class TmdbMovie{
    Integer vote_count;
    Integer id;
    Boolean video;
    String vote_average;
    String title;
    String popularity;
    String poster_path;
    String original_language;
    String originial_title;
    ArrayList<Integer> genre_ids;

    public Movie toMovie(){
        String baseImageURL = "https://image.tmdb.org/t/p/w500";
        Movie movie = new Movie(title);
        movie.setPoster(baseImageURL+poster_path);
        movie.setTmdbID(id);
        return movie;
    }
}
