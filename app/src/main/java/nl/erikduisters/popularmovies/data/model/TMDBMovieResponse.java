package nl.erikduisters.popularmovies.data.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

public class TMDBMovieResponse {
    private int page;
    private List<Movie> results;
    private int totalResults;
    private int totalPages;

    public @NonNull
    List<Movie> getResults() {
        if (results == null) {
            return new ArrayList<>();
        }

        return results;
    }
}
