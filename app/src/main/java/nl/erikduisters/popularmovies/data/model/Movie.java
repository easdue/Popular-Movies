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

    public String getPosterPath() {
        return posterPath;
    }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getOverview() {
        return overview;
    }
}
