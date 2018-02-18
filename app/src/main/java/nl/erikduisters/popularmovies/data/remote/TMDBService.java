package nl.erikduisters.popularmovies.data.remote;

import nl.erikduisters.popularmovies.data.model.Configuration;
import nl.erikduisters.popularmovies.data.model.TMDBMovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

public interface TMDBService {
    String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("configuration")
    Call<Configuration> getConfiguration(@Query("api_key") String api_key);

    @GET("movie/popular")
    Call<TMDBMovieResponse> getPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<TMDBMovieResponse> getTopRatedMovies(@Query("api_key") String api_key);
}
