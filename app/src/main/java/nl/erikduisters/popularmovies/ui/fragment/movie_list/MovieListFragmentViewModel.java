package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.telecom.Call;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.local.PreferenceManager;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.di.ActivityScope;
import nl.erikduisters.popularmovies.util.MyMenuItem;

/**
 * Created by Erik Duisters on 19-02-2018.
 */

@ActivityScope
public class MovieListFragmentViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private final PreferenceManager preferenceManager;
    private final MovieListFragmentViewState.Builder builder;
    private final MutableLiveData<MovieListFragmentViewState> viewState;
    private final Callback callback;

    @Inject
    MovieListFragmentViewModel(MovieRepository movieRepository, PreferenceManager preferenceManager) {
        this.movieRepository = movieRepository;
        this.preferenceManager = preferenceManager;

        this.builder = new MovieListFragmentViewState.Builder();

        boolean sortByHighestRated = preferenceManager.getSortByHighestRated();

        List<MyMenuItem> optionsMenu = new ArrayList<>();
        optionsMenu.add(new MyMenuItem(R.id.menu_sortOrder, true, true));
        optionsMenu.add(new MyMenuItem(R.id.menu_highestRated, true, true, sortByHighestRated));
        optionsMenu.add(new MyMenuItem(R.id.menu_mostPopular, true, true, !sortByHighestRated));

        builder.setOptionsMenu(optionsMenu);
        builder.setLoadingStatus();

        this.viewState = new MutableLiveData<>();
        this.viewState.setValue(builder.build());

        this.callback = new Callback();

        if (sortByHighestRated) {
            movieRepository.getTopRatedMovies(callback);
        } else {
            movieRepository.getPopularMovies(callback);
        }
    }

    LiveData<MovieListFragmentViewState> getViewState() {
        return viewState;
    }

    private class Callback implements MovieRepository.Callback {
        @Override
        public void onResponse(List<Movie> movieList) {
            builder.setSuccessStatus(movieList);

            viewState.setValue(builder.build());
        }

        @Override
        public void onError(@StringRes int errorLabel, @NonNull String errorArgument) {
            builder.setErrorStatus(errorLabel, errorArgument);

            viewState.setValue(builder.build());
        }
    }
}
