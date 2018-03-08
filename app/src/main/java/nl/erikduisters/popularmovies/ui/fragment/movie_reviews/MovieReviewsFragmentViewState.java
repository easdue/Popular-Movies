package nl.erikduisters.popularmovies.ui.fragment.movie_reviews;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import nl.erikduisters.popularmovies.data.model.Review;
import nl.erikduisters.popularmovies.data.model.Status;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

final class MovieReviewsFragmentViewState {
    final int movieId;
    final @Status int status;
    final @NonNull List<Review> reviewList;
    final @StringRes int emptyRevieListMessage;
    final @StringRes int errorLabel;
    final @NonNull String errorArgument;

    private MovieReviewsFragmentViewState(int movieId, @Status int status, @NonNull List<Review> reviewList, @StringRes int emptyRevieListMessage, @StringRes int errorLabel, @NonNull String errorArgument) {
        this.movieId = movieId;
        this.status = status;
        this.reviewList = reviewList;
        this.emptyRevieListMessage = emptyRevieListMessage;
        this.errorLabel = errorLabel;
        this.errorArgument = errorArgument;
    }

    static MovieReviewsFragmentViewState getErrorState(int movieId, @StringRes int errorLabel, @NonNull String errorArgument) {
        return new MovieReviewsFragmentViewState(movieId, Status.ERROR, new ArrayList<>(), 0, errorLabel, errorArgument);
    }

    static MovieReviewsFragmentViewState getLoadingState(int movieId) {
        return new MovieReviewsFragmentViewState(movieId, Status.LOADING, new ArrayList<>(), 0, 0, "");
    }

    static MovieReviewsFragmentViewState getSuccessState(int movieId, @NonNull List<Review> reviewList, @StringRes int emptyRevieListMessage) {
        return new MovieReviewsFragmentViewState(movieId, Status.SUCCESS, reviewList, emptyRevieListMessage,0,"");
    }
}
