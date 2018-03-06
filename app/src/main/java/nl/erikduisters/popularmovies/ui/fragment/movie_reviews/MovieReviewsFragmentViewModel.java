package nl.erikduisters.popularmovies.ui.fragment.movie_reviews;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.local.ReviewsRepository;
import nl.erikduisters.popularmovies.data.model.Review;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

//TODO: Make sure that on rotation change the data is re-used
@Singleton
public class MovieReviewsFragmentViewModel extends ViewModel {
    private final MutableLiveData<MovieReviewsFragmentViewState> reviewsViewState;
    private final ReviewsRepository reviewsRepository;

    @Inject
    MovieReviewsFragmentViewModel(ReviewsRepository reviewsRepository) {
        reviewsViewState = new MutableLiveData<>();

        this.reviewsRepository = reviewsRepository;
    }

    LiveData<MovieReviewsFragmentViewState> getReviewsViewState() { return reviewsViewState; }

    void setMovieId(int movieId) {
        MovieReviewsFragmentViewState viewState = reviewsViewState.getValue();

        if (viewState != null && viewState.movieId == movieId) {
            return;
        }

        if (movieId == MovieRepository.INVALID_MOVIE_ID) {
            reviewsViewState.setValue(MovieReviewsFragmentViewState.getErrorState(movieId, R.string.movie_id_invalid, ""));
        } else {
            reviewsViewState.setValue(MovieReviewsFragmentViewState.getLoadingState(movieId));

            reviewsRepository.getReviews(movieId, new ReviewsRepository.Callback() {
                @Override
                public void onResponse(@NonNull List<Review> reviews) {
                    reviewsViewState.setValue(MovieReviewsFragmentViewState.getSuccessState(movieId, reviews));
                }

                @Override
                public void onError(int error, @NonNull String errorArgument) {
                    reviewsViewState.setValue(MovieReviewsFragmentViewState.getErrorState(movieId, error, errorArgument));
                }
            });
        }
    }
}
