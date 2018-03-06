package nl.erikduisters.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Erik Duisters on 03-03-2018.
 */

public class TMDBReviewResponse {
    @SerializedName("id") private int movieId;
    private int page;
    private List<Review> results;
    private int total_pages;
    private int total_results;
}
