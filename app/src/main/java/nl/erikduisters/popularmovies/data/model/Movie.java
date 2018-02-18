package nl.erikduisters.popularmovies.data.model;

import java.util.List;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

public class Movie {
    private String posterPath;
    private boolean adult;
    private String overview;
    private String releaseDate;
    private List<Integer> genreIds;
    private int id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private float popularity;
    private int voteCount;
    private boolean video;
    private float vote_average;
}
