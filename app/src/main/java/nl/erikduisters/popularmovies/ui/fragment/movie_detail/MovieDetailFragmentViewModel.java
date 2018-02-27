package nl.erikduisters.popularmovies.ui.fragment.movie_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.model.Movie;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

@Singleton
public class MovieDetailFragmentViewModel extends ViewModel {
    private final MutableLiveData<MovieDetailFragmentViewState> viewState;
    private final MovieRepository movieRepository;

    @Inject
    MovieDetailFragmentViewModel(MovieRepository movieRepository) {
        Timber.d("New MovieDetailFragmentViewModel created");

        viewState = new MutableLiveData<>();
        viewState.setValue(MovieDetailFragmentViewState.getLoadingState());

        this.movieRepository = movieRepository;
    }

    LiveData<MovieDetailFragmentViewState> getViewState() {
        return viewState;
    }

    void setMovieId(int movieId) {
        if (viewState.getValue() != null && viewState.getValue().movie != null &&
                viewState.getValue().movie.getId() == movieId) {
            return;
        }

        if (movieId == MovieRepository.INVALID_MOVIE_ID) {
            viewState.setValue(MovieDetailFragmentViewState.getErrorState(R.string.movie_id_invalid, ""));
        } else {
            movieRepository.getMovie(movieId, new MovieRepository.Callback<Movie>() {
                @Override
                public void onResponse(Movie movie) {
                    viewState.setValue(MovieDetailFragmentViewState.getSuccessState(movie));
                }

                @Override
                public void onError(@StringRes int error, @NonNull String errorArgument) {
                    viewState.setValue(MovieDetailFragmentViewState.getErrorState(error, errorArgument));
                }
            });
        }
    }
}
