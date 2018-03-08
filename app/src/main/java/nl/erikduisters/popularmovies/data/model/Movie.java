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
    private float voteAverage;
    private transient boolean isFavorite;

    /*TODO: remove me
    protected Movie(Movie other) {
        this.posterPath = other.posterPath;
        this.adult = other.adult;
        this.overview = other.overview;
        this.releaseDate = other.releaseDate;
        this.genreIds = other.genreIds;
        this.id = other.id;
        this.originalTitle = other.originalTitle;
        this.originalLanguage  = other.originalLanguage;
        this.title = other.title;
        this.backdropPath = other.backdropPath;
        this.popularity = other.popularity;
        this.voteCount = other.voteCount;
        this.video = other.video;
        this.voteAverage = other.voteAverage;
        this.isFavorite = other.isFavorite;
    }
    */

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
        return voteAverage;
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

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }
}
