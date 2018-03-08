package nl.erikduisters.popularmovies.data.local;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.BuildConfig;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.database.MovieContract.MovieEntry;
import nl.erikduisters.popularmovies.data.model.Configuration;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.SortOrder;
import nl.erikduisters.popularmovies.data.model.TMDBMovieResponse;
import nl.erikduisters.popularmovies.data.remote.TMDBService;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 18-02-2018.
 */

@Singleton
public class MovieRepository extends ContentObserver {
    public static final int INVALID_MOVIE_ID = -1;

    private final TMDBService tmdbService;
    private final PreferenceManager preferenceManager;
    private final ContentResolver contentResolver;
    private final Executor executor;
    private final Handler handler;
    private List<Movie> movieList;
    private @SortOrder int sortOrder;
    private @Nullable Call<?> call;
    private @Nullable Callback<List<Movie>> favoriteMovieListCallback;

    @Inject
    MovieRepository(TMDBService tmdbService, PreferenceManager preferenceManager,
                    ContentResolver contentResolver, Executor executor, @Named("MainLooper") Handler handler) {
        super(handler);

        this.tmdbService = tmdbService;
        this.preferenceManager = preferenceManager;
        this.contentResolver = contentResolver;
        this.executor = executor;
        this.handler = handler;
    }

    public void getMoviesBySortOrder(@SortOrder int sortOrder, Callback<List<Movie>> callback) {
        Timber.d("getMoviesBySortOrder(%s)", sortOrder == SortOrder.TOP_RATED ? "TopRated" :
                sortOrder == SortOrder.POPULARITY ? "Popularity" : "Favorite");

        if (movieList != null && this.sortOrder == sortOrder) {
            callback.onResponse(movieList);
        }

        if (sortOrder == SortOrder.FAVORITE) {
            favoriteMovieListCallback = callback;
            contentResolver.registerContentObserver(MovieEntry.CONTENT_URI, true, this);
        } else {
            favoriteMovieListCallback = null;
            contentResolver.unregisterContentObserver(this);
        }

        switch (sortOrder) {
            case SortOrder.POPULARITY:
                getMovies(tmdbService.getPopularMovies(BuildConfig.TMDB_API_KEY), new TMDBMovieResponseCallback(callback, SortOrder.POPULARITY));
                break;
            case SortOrder.TOP_RATED:
                getMovies(tmdbService.getTopRatedMovies(BuildConfig.TMDB_API_KEY), new TMDBMovieResponseCallback(callback, SortOrder.TOP_RATED));
                break;
            case SortOrder.FAVORITE:
                executor.execute(new LoadFavoriteMovies(sortOrder, callback));
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
            @SortOrder int sortOrder = preferenceManager.getSortOrder();
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

    public void addToFavorites(Movie movie) {
        executor.execute(new InsertFavoriteMovie(movie));
    }

    public void removeFromFavorites(Movie movie) {
        executor.execute(new DeleteFavoriteMovie(movie));
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

    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }

    @Override
    public void onChange(boolean selfChange) {
        Timber.e("selfChange=%s, isUiThread=%s", selfChange ? "true" : "false", Looper.myLooper() == Looper.getMainLooper() ? "true" : "false");
        executor.execute(new LoadFavoriteMovies(sortOrder, favoriteMovieListCallback));
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        onChange(selfChange);
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

                    executor.execute(new UpdateFavoriteStatus(movieList, callback));
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
        void onResponse(@NonNull T response);
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
        public void onResponse(@NonNull List<Movie> response) {
            getMovie(movieId, callback);
        }

        @Override
        public void onError(@StringRes int error, @NonNull String errorArgument) {
            callback.onError(error, errorArgument);
        }
    }

    private class InsertFavoriteMovie implements Runnable {
        private final Movie movie;

        InsertFavoriteMovie(Movie movie) {
            this.movie = movie;
        }

        @Override
        public void run() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieEntry.COLUMN_ID, movie.getId());
            contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            contentValues.put(MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
            contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
            contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            contentValues.put(MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());

            contentResolver.insert(MovieEntry.CONTENT_URI, contentValues);
        }
    }

    private class DeleteFavoriteMovie implements Runnable {
        private final int movieId;

        DeleteFavoriteMovie(Movie movie) {
            movieId = movie.getId();
        }

        @Override
        public void run() {
            contentResolver.delete(ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movieId), null, null);
        }
    }

    private class UpdateFavoriteStatus implements Runnable {
        private final List<Movie> movieList;
        private final Callback<List<Movie>> callback;

        UpdateFavoriteStatus(List<Movie> movieList, Callback<List<Movie>> callback) {
            this.movieList = movieList;
            this.callback = callback;
        }

        @Override
        public void run() {
            Cursor cursor = MovieRepository.this.contentResolver.query(
                    MovieEntry.CONTENT_URI,
                    new String[] {MovieEntry.COLUMN_ID},
                    null,
                    null,
                    null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        int id = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_ID));

                        for (Movie movie : movieList) {
                            if (movie.getId() == id) {
                                movie.setFavorite(true);
                                break;
                            }
                        }

                        cursor.moveToNext();
                    }
                }

                cursor.close();
            }

            handler.post(() -> callback.onResponse(movieList));
        }
    }

    private class LoadFavoriteMovies implements Runnable {
        private final Callback<List<Movie>> callback;
        private final @SortOrder int sortOrder;

        LoadFavoriteMovies(@SortOrder int sortOrder, Callback<List<Movie>> callback) {
            this.sortOrder = sortOrder;
            this.callback = callback;
        }

        @Override
        public void run() {
            Cursor cursor = contentResolver.query(MovieEntry.CONTENT_URI, null, null, null, null);

            List<Movie> newMovieList = new ArrayList<>();

            if (cursor != null) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    newMovieList.add(getMovieFromCursor(cursor));

                    cursor.moveToNext();
                }
                cursor.close();
            }

            handler.post(() -> {
                MovieRepository.this.sortOrder = sortOrder;
                MovieRepository.this.movieList = newMovieList;

                callback.onResponse(newMovieList);
            });
        }

        private Movie getMovieFromCursor(Cursor cursor) {
            Movie movie = new Movie();

            movie.setId(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_ID)));
            movie.setFavorite(true);
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)));
            movie.setPopularity(cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)));
            movie.setVoteAverage(cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE)));
            movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT)));

            return movie;
        }
    }
}
