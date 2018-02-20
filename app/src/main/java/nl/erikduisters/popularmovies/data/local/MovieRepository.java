package nl.erikduisters.popularmovies.data.local;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.BuildConfig;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.TMDBMovieResponse;
import nl.erikduisters.popularmovies.data.remote.TMDBService;
import retrofit2.Call;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 18-02-2018.
 */

@Singleton
public class MovieRepository {
    @IntDef({SortOrder.POPULARITY, SortOrder.TOP_RATED})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SortOrder {
        int POPULARITY = 0;
        int TOP_RATED = 1;
    }

    private TMDBService tmdbService;
    private List<Movie> movieList;
    private @SortOrder int sortOrder;
    private Call<TMDBMovieResponse> call;

    @Inject
    MovieRepository(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    public void getPopularMovies(Callback callback) {
        getMoviesBySortOrder(SortOrder.POPULARITY, callback);
    }

    public void getTopRatedMovies(Callback callback) {
        getMoviesBySortOrder(SortOrder.TOP_RATED, callback);
    }

    private void getMoviesBySortOrder(@SortOrder int sortOrder, Callback callback) {
        Timber.d("getMoviesBySortOrder(%s)", sortOrder == SortOrder.TOP_RATED ? "TopRated" : "Popularity");

        if (movieList != null && this.sortOrder == sortOrder) {
            callback.onResponse(movieList);
        }

        switch (sortOrder) {
            case SortOrder.POPULARITY:
                enqueue(tmdbService.getPopularMovies(BuildConfig.TMDB_API_KEY), new RetrofitCallback(callback, SortOrder.POPULARITY));
                break;
            case SortOrder.TOP_RATED:
                enqueue(tmdbService.getTopRatedMovies(BuildConfig.TMDB_API_KEY), new RetrofitCallback(callback, SortOrder.TOP_RATED));
                break;
        }
    }

    private void cancelPendingCall() {
        if (call != null && !call.isCanceled()) {
            Timber.d("Cancelling pending call");
            call.cancel();
        }
    }

    private void enqueue(Call<TMDBMovieResponse> call, RetrofitCallback callback) {
        cancelPendingCall();

        this.call = call;
        call.enqueue(callback);
    }

    private class RetrofitCallback implements retrofit2.Callback<TMDBMovieResponse> {
        private final Callback callback;
        private @SortOrder int requestedSortOrder;

        RetrofitCallback(Callback callback, @StringRes int sortOrder) {
            this.callback = callback;
            this.requestedSortOrder = sortOrder;
        }

        @Override
        public void onResponse(@NonNull Call<TMDBMovieResponse> call, @NonNull retrofit2.Response<TMDBMovieResponse> response) {
            Timber.d("onResponse()");

            if (response.isSuccessful()) {
                TMDBMovieResponse tmdbMovieResponse = response.body();

                if (tmdbMovieResponse != null) {
                    movieList = tmdbMovieResponse.getResults();
                    sortOrder = requestedSortOrder;
                    callback.onResponse(tmdbMovieResponse.getResults());
                } else {
                    callback.onError(R.string.tmdb_api_call_failure, "Could not parse received response");
                }
            } else {
                //TODO: Properly handle this e.g. does response.message() contain the correct message or do I need to use response.errorBody()?
                callback.onError(R.string.tmdb_api_call_failure, response.message());
            }
        }

        @Override
        public void onFailure(@NonNull Call<TMDBMovieResponse> call, @NonNull Throwable t) {
            Timber.d("onFailure()");
            //TODO: Properly handle this (eg. when is this exactly called?)
            callback.onError(R.string.tmdb_api_call_failure, t.getMessage());
        }
    }

    public interface Callback {
        void onResponse(List<Movie> movieList);
        void onError(@StringRes int error, @NonNull String errorArgument);
    }
}
