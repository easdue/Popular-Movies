package nl.erikduisters.popularmovies.di;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivity;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivitySubcomponent;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivityViewModel;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.DetailActivity;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.DetailActivitySubcomponent;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.DetailActivityViewModel;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentViewModel;
import nl.erikduisters.popularmovies.ui.fragment.movie_list.MovieListFragmentViewModel;

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
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieListFragmentViewModel.class)
    abstract ViewModel bindMovieListFragmentViewModel(MovieListFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityKey(DetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindDetailActivityInjectorFactory(DetailActivitySubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ViewModelKey(DetailActivityViewModel.class)
    abstract ViewModel bindDetailActivityViewModel(DetailActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailFragmentViewModel.class)
    abstract ViewModel bindMovieDetailFragmentViewModel(MovieDetailFragmentViewModel viewModel);
}
