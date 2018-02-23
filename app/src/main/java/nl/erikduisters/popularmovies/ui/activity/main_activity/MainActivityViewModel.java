package nl.erikduisters.popularmovies.ui.activity.main_activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.di.ActivityScope;
import nl.erikduisters.popularmovies.util.MyMenuItem;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 03-12-2017.
 */
@Singleton
public final class MainActivityViewModel extends ViewModel {
    private MutableLiveData<MainActivityViewState> viewStateLiveData;

    @Inject
    MainActivityViewModel() {
        Timber.d("New MainActivityViewModel created");

        List<MyMenuItem> optionsMenu = new ArrayList<>();
        optionsMenu.add(new MyMenuItem(R.id.menu_about, true, true));

        MainActivityViewState viewState = new MainActivityViewState(optionsMenu);

        viewStateLiveData = new MutableLiveData<>();
        viewStateLiveData.setValue(viewState);
    }

    LiveData<MainActivityViewState> getViewState() {
        return viewStateLiveData;
    }
}
