package nl.erikduisters.popularmovies.ui.activity.movie_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

@Singleton
public class MovieDetailActivityViewModel extends ViewModel {
    private final MutableLiveData<MovieDetailActivityViewState> viewStateLiveData;

    @Inject
    MovieDetailActivityViewModel() {
        Timber.d("New DetailActivityViewModel created");

        viewStateLiveData = new MutableLiveData<>();
    }

    LiveData<MovieDetailActivityViewState> getViewState() {
        return viewStateLiveData;
    }

    void onHomeAsUpPressed() {
        viewStateLiveData.setValue(new MovieDetailActivityViewState.FinishViewState());
    }

    void onFinished() {
        viewStateLiveData.setValue(null);
    }
}
