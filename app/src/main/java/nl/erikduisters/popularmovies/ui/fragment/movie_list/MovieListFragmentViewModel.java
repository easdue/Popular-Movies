package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.local.PreferenceManager;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.MovieDetailActivity;
import nl.erikduisters.popularmovies.util.MyMenuItem;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 19-02-2018.
 */

@Singleton
public class MovieListFragmentViewModel extends AndroidViewModel {
    private final MovieRepository movieRepository;
    private final PreferenceManager preferenceManager;
    private final MovieListFragmentViewState.MovieViewState.Builder builder;
    private final MutableLiveData<MovieListFragmentViewState.MovieViewState> movieViewState;
    private final MutableLiveData<MovieListFragmentViewState.StartActivityViewState> startActivityViewState;
    private final Callback callback;
    private final List<MyMenuItem> optionsMenu;

    @Inject
    MovieListFragmentViewModel(Application application, MovieRepository movieRepository, PreferenceManager preferenceManager) {
        super(application);

        Timber.d("New MovieListFragmentViewModel created");

        this.movieRepository = movieRepository;
        this.preferenceManager = preferenceManager;

        builder = new MovieListFragmentViewState.MovieViewState.Builder();

        boolean sortByHighestRated = preferenceManager.getSortByHighestRated();

        optionsMenu = new ArrayList<>();
        optionsMenu.add(new MyMenuItem(R.id.menu_sortOrder, true, true));
        optionsMenu.add(new MyMenuItem(R.id.menu_highestRated, true, true));
        optionsMenu.add(new MyMenuItem(R.id.menu_mostPopular, true, true));

        updateOptionsMenu(sortByHighestRated);

        builder.setOptionsMenu(optionsMenu);
        builder.setLoadingStatus();

        movieViewState = new MutableLiveData<>();
        movieViewState.setValue(builder.build());

        startActivityViewState = new MutableLiveData<>();

        callback = new Callback();

        getMovies(sortByHighestRated);
    }

    LiveData<MovieListFragmentViewState.MovieViewState> getMovieViewState() { return movieViewState; }
    LiveData<MovieListFragmentViewState.StartActivityViewState> getStartActivityViewState() { return startActivityViewState; }

    private void updateOptionsMenu(boolean sortByHighestRated) {
        for (MyMenuItem item : optionsMenu) {
            if (item.id == R.id.menu_highestRated) {
                item.checked = sortByHighestRated;
            }

            if (item.id == R.id.menu_mostPopular) {
                item.checked = !sortByHighestRated;
            }
        }
    }

    private void getMovies(boolean sortByHighestRated) {
        if (sortByHighestRated) {
            movieRepository.getTopRatedMovies(callback);
        } else {
            movieRepository.getPopularMovies(callback);
        }
    }

    void onMenuItemClicked(@IdRes int menuId) {
        boolean sortByHighestRated = preferenceManager.getSortByHighestRated();

        if ((sortByHighestRated && menuId == R.id.menu_highestRated)
                || (!sortByHighestRated && menuId == R.id.menu_mostPopular)) {
            Timber.d("Not changing sort order it is already as requested");
            return;
        }


        sortByHighestRated = !sortByHighestRated;

        Timber.d("Changing sort order to: %s" , sortByHighestRated ? "Highest Rated" : "Popularity");

        preferenceManager.setSortByHighestRated(sortByHighestRated);

        updateOptionsMenu(sortByHighestRated);

        builder.setOptionsMenu(optionsMenu);
        builder.setLoadingStatus();

        movieViewState.setValue(builder.build());

        getMovies(sortByHighestRated);
    }

    public int getSpanCount() {
        return getApplication().getResources().getBoolean(R.bool.isLandscape) ? 3 : 2;
    }

    void onMovieClicked(@NonNull Movie movie) {
        startActivityViewState.setValue(new MovieListFragmentViewState.StartActivityViewState(movie.getId(), MovieDetailActivity.class));
    }

    void onActivityStarted() {
        startActivityViewState.setValue(null);
    }

    private class Callback implements MovieRepository.Callback<List<Movie>> {
        @Override
        public void onResponse(@NonNull List<Movie> movieList) {
            builder.setSuccessStatus(movieList);

            movieViewState.setValue(builder.build());
        }

        @Override
        public void onError(@StringRes int errorLabel, @NonNull String errorArgument) {
            builder.setErrorStatus(errorLabel, errorArgument);

            movieViewState.setValue(builder.build());
        }
    }
}
