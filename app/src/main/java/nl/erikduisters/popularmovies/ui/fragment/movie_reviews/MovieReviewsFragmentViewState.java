package nl.erikduisters.popularmovies.ui.fragment.movie_reviews;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.List;

import nl.erikduisters.popularmovies.data.model.Review;
import nl.erikduisters.popularmovies.data.model.Status;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

final class MovieReviewsFragmentViewState {
    final int movieId;
    final @Status int status;
    final @Nullable List<Review> reviewList;
    final @StringRes int errorLabel;
    final @NonNull String errorArgument;

    private MovieReviewsFragmentViewState(int movieId, @Status int status, @Nullable List<Review> reviewList, @StringRes int errorLabel, @NonNull String errorArgument) {
        this.movieId = movieId;
        this.status = status;
        this.reviewList = reviewList;
        this.errorLabel = errorLabel;
        this.errorArgument = errorArgument;
    }

    static MovieReviewsFragmentViewState getErrorState(int movieId, @StringRes int errorLabel, @NonNull String errorArgument) {
        return new MovieReviewsFragmentViewState(movieId, Status.ERROR, null, errorLabel, errorArgument);
    }

    static MovieReviewsFragmentViewState getLoadingState(int movieId) {
        return new MovieReviewsFragmentViewState(movieId, Status.LOADING, null, 0, "");
    }

    static MovieReviewsFragmentViewState getSuccessState(int movieId, List<Review> reviewList) {
        return new MovieReviewsFragmentViewState(movieId, Status.SUCCESS, reviewList, 0,"");
    }
}
