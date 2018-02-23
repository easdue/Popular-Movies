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
public class DetailActivityViewModel extends ViewModel {
    private MutableLiveData<DetailActivityViewState> viewStateLiveData;

    @Inject
    DetailActivityViewModel() {
        Timber.d("New DetailActivityViewModel created");

        viewStateLiveData = new MutableLiveData<>();
    }

    LiveData<DetailActivityViewState> getViewState() {
        return viewStateLiveData;
    }

    void onHomeAsUpPressed() {
        viewStateLiveData.setValue(new DetailActivityViewState.FinishViewState());
    }

    void onFinished() {
        viewStateLiveData.setValue(null);
    }
}
