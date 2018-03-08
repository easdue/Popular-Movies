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

    public String getPosterPath() {
        return posterPath;
    }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title; }

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public float getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(float voteAverage) { this.voteAverage = voteAverage; }

    public int getVoteCount() {
        return voteCount;
    }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }

    public float getPopularity() {
        return popularity;
    }
    public void setPopularity(float popularity) { this.popularity = popularity; }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) { this.overview = overview; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }
}
