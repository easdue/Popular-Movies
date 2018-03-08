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
import nl.erikduisters.popularmovies.data.model.SortOrder;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.MovieDetailActivity;
import nl.erikduisters.popularmovies.util.MyMenuItem;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 19-02-2018.
 */

//TODO: Show you don't have any favorites yet if there are none
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

        @SortOrder int sortOrder = preferenceManager.getSortOrder();

        optionsMenu = new ArrayList<>();
        optionsMenu.add(new MyMenuItem(R.id.menu_sortOrder, true, true));
        optionsMenu.add(new MyMenuItem(R.id.menu_favorites, true, true));
        optionsMenu.add(new MyMenuItem(R.id.menu_highestRated, true, true));
        optionsMenu.add(new MyMenuItem(R.id.menu_mostPopular, true, true));

        updateOptionsMenu(sortOrder);

        builder.setOptionsMenu(optionsMenu);
        builder.setLoadingStatus();

        movieViewState = new MutableLiveData<>();
        movieViewState.setValue(builder.build());

        startActivityViewState = new MutableLiveData<>();

        callback = new Callback();

        movieRepository.getMoviesBySortOrder(sortOrder, callback);
    }

    LiveData<MovieListFragmentViewState.MovieViewState> getMovieViewState() { return movieViewState; }
    LiveData<MovieListFragmentViewState.StartActivityViewState> getStartActivityViewState() { return startActivityViewState; }

    private void updateOptionsMenu(@SortOrder int sortOrder) {
        for (MyMenuItem item : optionsMenu) {
            if (item.id == R.id.menu_favorites) {
                item.checked = sortOrder == SortOrder.FAVORITE;
            }
            if (item.id == R.id.menu_highestRated) {
                item.checked = sortOrder == SortOrder.TOP_RATED;
            }

            if (item.id == R.id.menu_mostPopular) {
                item.checked = sortOrder == SortOrder.POPULARITY;
            }
        }
    }

    void onMenuItemClicked(@IdRes int menuId) {
        @SortOrder int currentSortOrder = preferenceManager.getSortOrder();

        @SortOrder int newSortOrder = menuIdToSortorder(menuId, currentSortOrder);

        if (newSortOrder == currentSortOrder) {
            Timber.d("Not changing sort order it is already as requested");
            return;
        }

        Timber.d("Changing sort order to: %s" , newSortOrder == SortOrder.TOP_RATED ? "Highest Rated" :
                newSortOrder == SortOrder.POPULARITY ? "Popularity" : "Favorite");

        preferenceManager.setSortOrder(newSortOrder);

        updateOptionsMenu(newSortOrder);

        builder.setOptionsMenu(optionsMenu);
        builder.setLoadingStatus();

        movieViewState.setValue(builder.build());

        movieRepository.getMoviesBySortOrder(newSortOrder, callback);
    }

    private @SortOrder int menuIdToSortorder(int menuId, @SortOrder int fallbackSortOrder) {
        switch (menuId) {
            case R.id.menu_favorites:
                return SortOrder.FAVORITE;
            case R.id.menu_highestRated:
                return SortOrder.TOP_RATED;
            case R.id.menu_mostPopular:
                return SortOrder.POPULARITY;
            default:
                return fallbackSortOrder;
        }
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
            @SortOrder int sortOrder = preferenceManager.getSortOrder();
            @StringRes int emptyListResId = 0;

            if (movieList.size() == 0 && sortOrder == SortOrder.FAVORITE) {
                emptyListResId = R.string.no_favorites_movies_yet;
            }

            builder.setSuccessStatus(movieList, emptyListResId);

            movieViewState.setValue(builder.build());
        }

        @Override
        public void onError(@StringRes int errorLabel, @NonNull String errorArgument) {
            builder.setErrorStatus(errorLabel, errorArgument);

            movieViewState.setValue(builder.build());
        }
    }
}
