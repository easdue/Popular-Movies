package nl.erikduisters.popularmovies.data.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.BuildConfig;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.Review;
import nl.erikduisters.popularmovies.data.model.TMDBReviewResponse;
import nl.erikduisters.popularmovies.data.remote.TMDBService;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

@Singleton
public class ReviewsRepository {
    private final TMDBService tmdbService;
    private @Nullable Call<TMDBReviewResponse> call;

    @Inject
    ReviewsRepository(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    public void getReviews(int movieId, Callback callback) {
        if (call != null && !call.isExecuted()) {
            call.cancel();
        }

        call = tmdbService.getReviews(movieId, BuildConfig.TMDB_API_KEY);
        call.enqueue(new TMDBReviewResponseCallback(callback));
    }

    public interface Callback {
        void onResponse(@NonNull List<Review> reviews);
        void onError(@StringRes int error, @NonNull String errorArgument);
    }

    private class TMDBReviewResponseCallback implements retrofit2.Callback<TMDBReviewResponse> {
        private Callback callback;

        TMDBReviewResponseCallback(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<TMDBReviewResponse> call, Response<TMDBReviewResponse> response) {
            Timber.d("onResponse()");

            ReviewsRepository.this.call = null;

            if (response.isSuccessful()) {
                TMDBReviewResponse tmdbReviewResponse = response.body();

                if (tmdbReviewResponse != null) {
                    callback.onResponse(tmdbReviewResponse.getResults());
                } else {
                    callback.onError(R.string.tmdb_api_call_failure, "Could not parse received response");
                }
            } else {
                callback.onError(R.string.tmdb_api_call_failure, response.message());
            }
        }

        @Override
        public void onFailure(Call<TMDBReviewResponse> call, Throwable t) {
            Timber.d("onFailure()");

            ReviewsRepository.this.call = null;

            callback.onError(R.string.tmdb_api_call_failure, t.getMessage());
        }
    }
}
