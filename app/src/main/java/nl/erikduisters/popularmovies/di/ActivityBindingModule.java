package nl.erikduisters.popularmovies.di;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivity;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivitySubcomponent;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.MovieDetailActivity;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.MovieDetailActivitySubcomponent;

/**
 * Created by Erik Duisters on 25-11-2017.
 */

@Module
abstract class ActivityBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(MainActivitySubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(MovieDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindDetailActivityInjectorFactory(MovieDetailActivitySubcomponent.Builder builder);
}
