package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.util.MyMenuItem;

/**
 * Created by Erik Duisters on 19-02-2018.
 */

final class MovieListFragmentViewState {
    @IntDef({Status.LOADING, Status.SUCCESS, Status.ERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface Status {
        int LOADING = 0;
        int SUCCESS = 1;
        int ERROR = 2;
    }

    final @Status int status;
    final @NonNull List<Movie> movieList;
    final @StringRes int errorLabel;
    final @NonNull String errorArgument;
    final List<MyMenuItem> optionsMenu;

    private MovieListFragmentViewState(Builder builder) {
        status = builder.status;
        movieList = builder.movieList;
        errorLabel = builder.errorLabel;
        errorArgument = builder.errorArgument;
        optionsMenu = builder.optionsMenu;
    }

    public static final class Builder {
        private int status;
        private @NonNull List<Movie> movieList;
        private int errorLabel;
        private @NonNull String errorArgument;
        private @NonNull List<MyMenuItem> optionsMenu;

        public Builder() {
            movieList = new ArrayList<>();
            errorArgument = "";
            optionsMenu = new ArrayList<>();
        }

        public Builder(@NonNull MovieListFragmentViewState from) {
            this.status = from.status;
            this.movieList = from.movieList;
            this.errorLabel = from.errorLabel;
            this.errorArgument = from.errorArgument;
            this.optionsMenu = from.optionsMenu;
        }

        public Builder setErrorStatus(@StringRes int errorLabel, @NonNull String errorArgument) {
            this.status = Status.ERROR;
            this.errorLabel = errorLabel;
            this.errorArgument = errorArgument;
            this.movieList.clear();

            return this;
        }

        public Builder setLoadingStatus() {
            this.status = Status.LOADING;
            this.errorLabel = 0;
            this.errorArgument = "";

            return this;
        }

        public Builder setOptionsMenu(@NonNull List<MyMenuItem> optionsMenu) {
            this.optionsMenu = optionsMenu;

            return this;
        }

        public Builder setSuccessStatus(List<Movie> movieList) {
            this.status = Status.SUCCESS;
            this.movieList = movieList;
            this.errorLabel = 0;
            this.errorArgument = "";

            return this;
        }

        public MovieListFragmentViewState build() {
            return new MovieListFragmentViewState(this);
        }
    }
}
