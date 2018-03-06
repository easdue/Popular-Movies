package nl.erikduisters.popularmovies.data.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.BuildConfig;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.TMDBVideoResponse;
import nl.erikduisters.popularmovies.data.model.Video;
import nl.erikduisters.popularmovies.data.remote.TMDBService;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 05-03-2018.
 */

@Singleton
public class TrailerRepository {
    private final TMDBService tmdbService;
    private @Nullable Call<TMDBVideoResponse> call;

    @Inject
    TrailerRepository(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    public void getTrailers(int movieId, Callback callback) {
        if (call != null && !call.isExecuted()) {
            call.cancel();
        }

        call = tmdbService.getVideos(movieId, BuildConfig.TMDB_API_KEY);
        call.enqueue(new TMDBVideoResponseCallback(callback));
    }

    public interface Callback {
        void onResponse(@NonNull List<Video> trailers);
        void onError(@StringRes int error, @NonNull String errorArgument);
    }

    private class TMDBVideoResponseCallback implements retrofit2.Callback<TMDBVideoResponse> {
        private Callback callback;

        TMDBVideoResponseCallback(Callback callback) { this.callback = callback; }

        @Override
        public void onResponse(Call<TMDBVideoResponse> call, Response<TMDBVideoResponse> response) {
            Timber.d("onResponse()");

            TrailerRepository.this.call = null;

            if (response.isSuccessful()) {
                TMDBVideoResponse tmdbVideoResponse = response.body();

                if (tmdbVideoResponse != null) {
                    callback.onResponse(removeNonTrailers(tmdbVideoResponse.getResults()));
                } else {
                    callback.onError(R.string.tmdb_api_call_failure, "Could not parse received response");
                }
            } else {
                callback.onError(R.string.tmdb_api_call_failure, response.message());
            }
        }

        @Override
        public void onFailure(Call<TMDBVideoResponse> call, Throwable t) {
            Timber.d("onFailure()");

            TrailerRepository.this.call = null;

            callback.onError(R.string.tmdb_api_call_failure, t.getMessage());
        }

        private List<Video> removeNonTrailers(List<Video> videoList) {
            Iterator<Video> it = videoList.iterator();

            while (it.hasNext()) {
                Video video = it.next();

                if (!video.isTrailer()) {
                    it.remove();
                }
            }

            return videoList;
        }
    }
}
