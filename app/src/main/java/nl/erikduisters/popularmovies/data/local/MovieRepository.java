package nl.erikduisters.popularmovies.data.local;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.BuildConfig;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.Configuration;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.TMDBMovieResponse;
import nl.erikduisters.popularmovies.data.remote.TMDBService;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 18-02-2018.
 */

@Singleton
public class MovieRepository {
    public static final int INVALID_MOVIE_ID = -1;

    @IntDef({SortOrder.POPULARITY, SortOrder.TOP_RATED})
    @Retention(RetentionPolicy.SOURCE)
    private @interface SortOrder {
        int POPULARITY = 0;
        int TOP_RATED = 1;
    }

    private final TMDBService tmdbService;
    private final PreferenceManager preferenceManager;
    private List<Movie> movieList;
    private @SortOrder int sortOrder;
    private @Nullable Call<?> call;

    @Inject
    MovieRepository(TMDBService tmdbService, PreferenceManager preferenceManager) {
        this.tmdbService = tmdbService;
        this.preferenceManager = preferenceManager;
    }

    public void getPopularMovies(Callback<List<Movie>> callback) {
        getMoviesBySortOrder(SortOrder.POPULARITY, callback);
    }

    public void getTopRatedMovies(Callback<List<Movie>> callback) {
        getMoviesBySortOrder(SortOrder.TOP_RATED, callback);
    }

    private void getMoviesBySortOrder(@SortOrder int sortOrder, Callback<List<Movie>> callback) {
        Timber.d("getMoviesBySortOrder(%s)", sortOrder == SortOrder.TOP_RATED ? "TopRated" : "Popularity");

        if (movieList != null && this.sortOrder == sortOrder) {
            callback.onResponse(movieList);
        }

        switch (sortOrder) {
            case SortOrder.POPULARITY:
                getMovies(tmdbService.getPopularMovies(BuildConfig.TMDB_API_KEY), new TMDBMovieResponseCallback(callback, SortOrder.POPULARITY));
                break;
            case SortOrder.TOP_RATED:
                getMovies(tmdbService.getTopRatedMovies(BuildConfig.TMDB_API_KEY), new TMDBMovieResponseCallback(callback, SortOrder.TOP_RATED));
                break;
        }
    }

    private void getMovies(Call<TMDBMovieResponse> call, TMDBMovieResponseCallback callback) {
        cancelPendingCall();

        if (isConfigurationOutdated()) {
            getConfiguration(call, callback);
            return;
        }

        this.call = call;
        call.enqueue(callback);
    }

    private void cancelPendingCall() {
        if (call != null && !call.isExecuted()) {
            Timber.d("Cancelling pending call");
            call.cancel();
        }
    }

    private boolean isConfigurationOutdated() {
        Date lastReadData = preferenceManager.getTMDBConfigurationReadDate();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        return lastReadData.before(calendar.getTime());
    }

    private void getConfiguration(Call<TMDBMovieResponse> movieResponseCall, TMDBMovieResponseCallback movieResponseCallback) {
        Call<Configuration> configurationCall = tmdbService.getConfiguration(BuildConfig.TMDB_API_KEY);
        this.call = configurationCall;

        configurationCall.enqueue(new retrofit2.Callback<Configuration>() {
            @Override
            public void onResponse(@NonNull Call<Configuration> call, @NonNull Response<Configuration> response) {
                MovieRepository.this.call = null;

                if (response.isSuccessful()) {
                    Configuration configuration = response.body();

                    if (configuration != null) {
                        preferenceManager.setTMDBConfiguration(configuration);
                        getMovies(movieResponseCall, movieResponseCallback);
                    } else {
                        movieResponseCallback.onFailure(movieResponseCall, new Throwable("Could not parse received response"));
                    }
                } else {
                    movieResponseCallback.onFailure(movieResponseCall, new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Configuration> call, @NonNull Throwable t) {
                MovieRepository.this.call = null;

                movieResponseCallback.onFailure(movieResponseCall, t);
            }
        });
    }

    public void getMovie(int movieId, Callback<Movie> callback) {
        if (movieList == null) {
            @SortOrder int sortOrder = preferenceManager.getSortByHighestRated() ? SortOrder.TOP_RATED : SortOrder.POPULARITY;
            getMoviesBySortOrder(sortOrder, new CallbackWrapper(callback, movieId));

            return;
        }

        Movie movie = getMovieById(movieId);

        if (movie == null) {
            callback.onError(R.string.movie_id_unknown, "");
        } else {
            callback.onResponse(movie);
        }
    }

    private @Nullable Movie getMovieById(int id) {
        if (movieList == null) {
            return null;
        }

        for (Movie movie : movieList) {
            if (movie.getId() == id) {
                return movie;
            }
        }

        return null;
    }

    private class TMDBMovieResponseCallback implements retrofit2.Callback<TMDBMovieResponse> {
        private final Callback<List<Movie>> callback;
        private final @SortOrder int requestedSortOrder;

        TMDBMovieResponseCallback(Callback<List<Movie>> callback, @StringRes int sortOrder) {
            this.callback = callback;
            this.requestedSortOrder = sortOrder;
        }

        @Override
        public void onResponse(@NonNull Call<TMDBMovieResponse> call, @NonNull retrofit2.Response<TMDBMovieResponse> response) {
            Timber.d("onResponse()");

            MovieRepository.this.call = null;

            if (response.isSuccessful()) {
                TMDBMovieResponse tmdbMovieResponse = response.body();

                if (tmdbMovieResponse != null) {
                    movieList = tmdbMovieResponse.getResults();
                    updatePosterPaths();

                    sortOrder = requestedSortOrder;
                    callback.onResponse(movieList);
                } else {
                    callback.onError(R.string.tmdb_api_call_failure, "Could not parse received response");
                }
            } else {
                callback.onError(R.string.tmdb_api_call_failure, response.message());
            }
        }

        @Override
        public void onFailure(@NonNull Call<TMDBMovieResponse> call, @NonNull Throwable t) {
            Timber.d("onFailure()");

            MovieRepository.this.call = null;

            callback.onError(R.string.tmdb_api_call_failure, t.getMessage());
        }
    }

    private void updatePosterPaths() {
        Configuration configuration = preferenceManager.getTMDBConfiguration();

        if (configuration == null) {
            return;
        }

        Configuration.Images images = configuration.getImages();

        StringBuilder sb = new StringBuilder();
        sb.append(images.getBaseUrl());
        sb.append(getPosterSize(images.getPosterSizes()));

        int length = sb.length();

        for (Movie movie : movieList) {
            sb.setLength(length);
            sb.append(movie.getPosterPath());
            movie.setPosterPath(sb.toString());
        }
    }

    private String getPosterSize(List<String> posterSizes) {
        final int preferredSize = 185;
        int bestMatchIndex = -1;
        int bestMatchDiff = Integer.MAX_VALUE;

        for (int i = 0; i < posterSizes.size(); i++) {
            String posterSize = posterSizes.get(i).toLowerCase();

            if (posterSize.startsWith("w")) {
                int size = Integer.parseInt(posterSize.substring(1));
                int sizeDiff = Math.abs(preferredSize - size);

                if (sizeDiff < bestMatchDiff) {
                    bestMatchDiff = sizeDiff;
                    bestMatchIndex = i;
                }
            }
        }

        if (bestMatchIndex == -1) {
            bestMatchIndex = posterSizes.size() - 1;
        }

        return posterSizes.get(bestMatchIndex);
    }

    public interface Callback<T> {
        void onResponse(T response);
        void onError(@StringRes int error, @NonNull String errorArgument);
    }

    private class CallbackWrapper implements Callback<List<Movie>> {
        private final Callback<Movie> callback;
        private final int movieId;

        CallbackWrapper(Callback<Movie> callback, int movieId) {
            this.callback = callback;
            this.movieId = movieId;
        }

        @Override
        public void onResponse(List<Movie> response) {
            getMovie(movieId, callback);
        }

        @Override
        public void onError(@StringRes int error, @NonNull String errorArgument) {
            callback.onError(error, errorArgument);
        }
    }
}
