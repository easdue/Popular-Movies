package nl.erikduisters.popularmovies.data.remote;

import nl.erikduisters.popularmovies.data.model.Configuration;
import nl.erikduisters.popularmovies.data.model.TMDBMovieResponse;
import nl.erikduisters.popularmovies.data.model.TMDBReviewResponse;
import nl.erikduisters.popularmovies.data.model.TMDBVideoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

//TODO: Use a OkHttp interceptor to set the api_key query parameter
public interface TMDBService {
    String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("configuration")
    Call<Configuration> getConfiguration(@Query("api_key") String api_key);

    @GET("movie/popular")
    Call<TMDBMovieResponse> getPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<TMDBMovieResponse> getTopRatedMovies(@Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    Call<TMDBVideoResponse> getVideos(@Path("id") int movieId, @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<TMDBReviewResponse> getReviews(@Path("id") int movieId, @Query("api_key") String api_key);
}
