package nl.erikduisters.popularmovies.ui.fragment.movie_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.local.TrailerRepository;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.Video;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentViewState.MovieViewState;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentViewState.StartActivityViewState;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentViewState.ToastViewState;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentViewState.TrailerViewState;
import nl.erikduisters.popularmovies.util.MyMenuItem;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

@Singleton
public class MovieDetailFragmentViewModel extends ViewModel {
    private final MutableLiveData<MovieViewState> movieViewState;
    private final MutableLiveData<TrailerViewState> trailerViewState;
    private final MutableLiveData<StartActivityViewState> startActivityViewState;
    private final MutableLiveData<ToastViewState> toastViewState;
    private final MovieRepository movieRepository;
    private final TrailerRepository trailerRepository;
    private final List<MyMenuItem> optionsMenu;

    @Inject
    MovieDetailFragmentViewModel(MovieRepository movieRepository, TrailerRepository trailerRepository) {
        Timber.d("New MovieDetailFragmentViewModel created");

        movieViewState = new MutableLiveData<>();
        movieViewState.setValue(MovieViewState.getLoadingState());

        trailerViewState = new MutableLiveData<>();
        startActivityViewState = new MutableLiveData<>();
        toastViewState = new MutableLiveData<>();

        this.movieRepository = movieRepository;
        this.trailerRepository = trailerRepository;

        optionsMenu = new ArrayList<>();
        optionsMenu.add(new MyMenuItem(R.id.menu_share, true, false));
    }

    LiveData<MovieViewState> getMovieViewState() {
        return movieViewState;
    }
    LiveData<TrailerViewState> getTrailerViewState() { return trailerViewState; }
    LiveData<StartActivityViewState> getStartActivityViewState() { return startActivityViewState; }
    LiveData<ToastViewState> getToastViewState() { return toastViewState; }

    void setMovieId(int movieId) {
        if (movieViewState.getValue() != null && movieViewState.getValue().movie != null &&
                movieViewState.getValue().movie.getId() == movieId) {
            return;
        }

        if (movieId == MovieRepository.INVALID_MOVIE_ID) {
            movieViewState.setValue(MovieViewState.getErrorState(R.string.movie_id_invalid, ""));
        } else {
            movieRepository.getMovie(movieId, new MovieRepository.Callback<Movie>() {
                @Override
                public void onResponse(@NonNull Movie movie) {
                    movieViewState.setValue(MovieViewState.getSuccessState(movie));
                    optionsMenu.get(0).visible = false;
                    trailerViewState.setValue(TrailerViewState.getLoadingState(optionsMenu));
                    loadTrailers(movie.getId());
                }

                @Override
                public void onError(@StringRes int error, @NonNull String errorArgument) {
                    movieViewState.setValue(MovieViewState.getErrorState(error, errorArgument));
                }
            });
        }
    }

    private void loadTrailers(int movieId) {
        trailerRepository.getTrailers(movieId, new TrailerRepository.Callback() {
            @Override
            public void onResponse(@NonNull List<Video> trailers) {
                optionsMenu.get(0).visible = !trailers.isEmpty();

                trailerViewState.setValue(TrailerViewState.getSuccessState(trailers, trailers.isEmpty() ? R.string.no_trailers_available : 0, optionsMenu));
            }

            @Override
            public void onError(@StringRes int error, @NonNull String errorArgument) {
                optionsMenu.get(0).visible = false;

                trailerViewState.setValue(TrailerViewState.getErrorState(error, errorArgument, optionsMenu));
            }
        });
    }

    void onTrailerClicked(Video video) {
        startActivityViewState.setValue(new StartActivityViewState(video.getVideoUri()));
    }

    void onActivityStarted() {
        startActivityViewState.setValue(null);
    }

    void onActivityStartFailed() {
        toastViewState.setValue(new ToastViewState(R.string.cannot_start_activity_to_view_trailer, false));
    }

    void onToastDisplayed() {
        toastViewState.setValue(null);
    }

    void onFavoriteClicked(Movie movie) {
        movie.setFavorite(!movie.isFavorite());
        movieViewState.setValue(MovieViewState.getSuccessState(movie));

        if (movie.isFavorite()) {
            movieRepository.addToFavorites(movie);
        } else {
            movieRepository.removeFromFavorites(movie);
        }
    }
}
