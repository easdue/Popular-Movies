package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.Status;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.MovieDetailActivity;
import nl.erikduisters.popularmovies.util.MyMenuItem;

/**
 * Created by Erik Duisters on 19-02-2018.
 */

public interface MovieListFragmentViewState {
    final class MovieViewState implements MovieListFragmentViewState {
        final @Status int status;
        final @NonNull List<Movie> movieList;
        final @StringRes int emptyMovieListText;
        final @StringRes int errorLabel;
        final @NonNull String errorArgument;
        final List<MyMenuItem> optionsMenu;

        MovieViewState(Builder builder) {
            status = builder.status;
            movieList = builder.movieList;
            emptyMovieListText = builder.emptyMovieListText;
            errorLabel = builder.errorLabel;
            errorArgument = builder.errorArgument;
            optionsMenu = builder.optionsMenu;
        }

        public static final class Builder {
            private int status;
            private @NonNull List<Movie> movieList;
            private @StringRes int emptyMovieListText;
            private int errorLabel;
            private @NonNull String errorArgument;
            private @NonNull List<MyMenuItem> optionsMenu;

            public Builder() {
                movieList = new ArrayList<>();
                errorArgument = "";
                optionsMenu = new ArrayList<>();
            }

            public Builder(@NonNull MovieViewState from) {
                this.status = from.status;
                this.movieList = from.movieList;
                this.emptyMovieListText = from.emptyMovieListText;
                this.errorLabel = from.errorLabel;
                this.errorArgument = from.errorArgument;
                this.optionsMenu = from.optionsMenu;
            }

            void setErrorStatus(@StringRes int errorLabel, @NonNull String errorArgument) {
                this.status = Status.ERROR;
                this.errorLabel = errorLabel;
                this.errorArgument = errorArgument;
                this.movieList.clear();
            }

            void setLoadingStatus() {
                this.status = Status.LOADING;
                this.errorLabel = 0;
                this.errorArgument = "";
            }

            void setOptionsMenu(@NonNull List<MyMenuItem> optionsMenu) {
                this.optionsMenu = optionsMenu;
            }

            void setSuccessStatus(List<Movie> movieList, @StringRes int emptyMovieListText) {
                this.status = Status.SUCCESS;
                this.movieList = movieList;
                this.emptyMovieListText = emptyMovieListText;
                this.errorLabel = 0;
                this.errorArgument = "";
            }

            MovieViewState build() {
                return new MovieViewState(this);
            }
        }
    }

    final class StartActivityViewState implements MovieListFragmentViewState {
        final int movieId;
        final Class<?> activityClass;

        StartActivityViewState(int movieId, Class<?> activityClass) {
            this.movieId = movieId;
            this.activityClass = activityClass;
        }

        Intent getIntent(Context ctx) {
            Intent intent = new Intent(ctx, activityClass);
            intent.putExtra(MovieDetailActivity.KEY_MOVIE_ID, movieId);

            return intent;
        }
    }
}
