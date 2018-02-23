package nl.erikduisters.popularmovies.ui.fragment.movie_detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.Status;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

final class MovieDetailFragmentViewState {
    final @Status int status;
    final @Nullable Movie movie;
    final @StringRes int errorLabel;
    final @NonNull String errorArgument;

    private MovieDetailFragmentViewState(@Status int status, @Nullable Movie movie, @StringRes int errorLabel, @NonNull String errorArgument) {
        this.status = status;
        this.movie = movie;
        this.errorLabel = errorLabel;
        this.errorArgument = errorArgument;
    }

    static MovieDetailFragmentViewState getErrorState(@StringRes int errorLabel, @NonNull String errorArgument) {
        return new MovieDetailFragmentViewState(Status.ERROR, null, errorLabel, errorArgument);
    }

    static MovieDetailFragmentViewState getLoadingState() {
        return new MovieDetailFragmentViewState(Status.LOADING, null, 0, "");
    }

    static MovieDetailFragmentViewState getSuccessState(@NonNull Movie movie) {
        return new MovieDetailFragmentViewState(Status.SUCCESS, movie, 0, "");
    }
}
