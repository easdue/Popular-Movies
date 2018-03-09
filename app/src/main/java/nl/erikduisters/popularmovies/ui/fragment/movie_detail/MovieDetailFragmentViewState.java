package nl.erikduisters.popularmovies.ui.fragment.movie_detail;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.List;

import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.Status;
import nl.erikduisters.popularmovies.data.model.Video;
import nl.erikduisters.popularmovies.ui.fragment.movie_list.MovieListFragmentViewState;
import nl.erikduisters.popularmovies.util.MyMenuItem;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

interface MovieDetailFragmentViewState {
    final class MovieViewState implements MovieDetailFragmentViewState {
        final @Status int status;
        final @Nullable Movie movie;
        final @StringRes int errorLabel;
        final @NonNull String errorArgument;

        private MovieViewState(@Status int status, @Nullable Movie movie, @StringRes int errorLabel,
                               @NonNull String errorArgument) {
            this.status = status;
            this.movie = movie;
            this.errorLabel = errorLabel;
            this.errorArgument = errorArgument;
        }

        static MovieViewState getErrorState(@StringRes int errorLabel, @NonNull String errorArgument) {
            return new MovieViewState(Status.ERROR, null, errorLabel, errorArgument);
        }

        static MovieViewState getLoadingState() {
            return new MovieViewState(Status.LOADING, null, 0, "");
        }

        static MovieViewState getSuccessState(@NonNull Movie movie) {
            return new MovieViewState(Status.SUCCESS, movie, 0, "");
        }
    }

    final class TrailerViewState implements MovieDetailFragmentViewState {
        final @Status int status;
        final @Nullable List<Video> trailerList;
        final @StringRes int emptyTrailerListMessage;
        final @StringRes int errorLabel;
        final @NonNull String errorArgument;
        final @NonNull List<MyMenuItem> optionsMenu;

        private TrailerViewState(@Status int status, @Nullable List<Video> trailers, @StringRes int emptyTrailerListMessage,
                                 @StringRes int errorLabel, @NonNull String errorArgument, @NonNull List<MyMenuItem> optionsMenu) {
            this.status = status;
            this.trailerList = trailers;
            this.emptyTrailerListMessage = emptyTrailerListMessage;
            this.errorLabel = errorLabel;
            this.errorArgument = errorArgument;
            this.optionsMenu = optionsMenu;
        }

        static TrailerViewState getErrorState(@StringRes int errorLabel, @NonNull String errorArgument, @NonNull List<MyMenuItem> optionsMenu) {
            return new TrailerViewState(Status.ERROR, null, 0, errorLabel, errorArgument, optionsMenu);
        }

        static TrailerViewState getLoadingState(@NonNull List<MyMenuItem> optionsMenu) {
            return new TrailerViewState(Status.LOADING, null, 0, 0, "", optionsMenu);
        }

        static TrailerViewState getSuccessState(@NonNull List<Video> trailers,
                                                @StringRes int emptyTrailerListMessage, List<MyMenuItem> optionsMenu) {
            return new TrailerViewState(Status.SUCCESS, trailers, emptyTrailerListMessage, 0, "", optionsMenu);
        }
    }

    final class StartActivityViewState implements MovieListFragmentViewState {
        final Uri videoUri;

        StartActivityViewState(@NonNull Uri videoUri) {
            this.videoUri = videoUri;
        }

        Intent getIntent() {
            return new Intent(Intent.ACTION_VIEW, videoUri);
        }
    }

    final class ToastViewState implements MovieListFragmentViewState {
        @StringRes final int message;
        final boolean displayShortMessage;

        ToastViewState(@StringRes int message, boolean displayShortMessage) {
            this.message = message;
            this.displayShortMessage = displayShortMessage;
        }
    }
}
