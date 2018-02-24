package nl.erikduisters.popularmovies.di;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import nl.erikduisters.popularmovies.ui.activity.main_activity.MainActivityViewModel;
import nl.erikduisters.popularmovies.ui.activity.movie_detail.MovieDetailActivityViewModel;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentViewModel;
import nl.erikduisters.popularmovies.ui.fragment.movie_list.MovieListFragmentViewModel;

/**
 * Created by Erik Duisters on 24-02-2018.
 */

@Module
abstract class ViewModelBindingModule {
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
    @ViewModelKey(MovieDetailActivityViewModel.class)
    abstract ViewModel bindDetailActivityViewModel(MovieDetailActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailFragmentViewModel.class)
    abstract ViewModel bindMovieDetailFragmentViewModel(MovieDetailFragmentViewModel viewModel);
}
