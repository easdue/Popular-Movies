package nl.erikduisters.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Erik Duisters on 03-03-2018.
 */

public class TMDBVideoResponse {
    @SerializedName("id") private int movieId;
    private List<Video> results;

    public List<Video> getResults() { return results; }
}
