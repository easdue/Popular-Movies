package nl.erikduisters.popularmovies.data.model;

import java.util.List;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

public class TMDBMovieResponse {
    private int page;
    private List<Movie> results;
    private int totalResults;
    private int totalPages;
}
