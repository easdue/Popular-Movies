package nl.erikduisters.popularmovies.di;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivity;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivitySubcomponent;

/**
 * Created by Erik Duisters on 25-11-2017.
 */

@Module
abstract class ActivityBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(MainActivitySubcomponent.Builder builder);

    /*
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel viewModel);
    */
    // Other Activities go here
}
