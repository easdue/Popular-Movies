package nl.erikduisters.popularmovies.ui.activity.main_activity;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import nl.erikduisters.popularmovies.ui.fragment.movie_list.MovieListFragment;
import nl.erikduisters.popularmovies.ui.fragment.movie_list.MovieListFragmentSubComponent;

/**
 * Created by Erik Duisters on 08-12-2017.
 */

@Module(subcomponents = {MovieListFragmentSubComponent.class})
abstract class MainActivityBindingModule {
    @Binds
    @IntoMap
    @FragmentKey(MovieListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindMainActivityFragmentInjectorFactory(MovieListFragmentSubComponent.Builder builder);

    /*
    @Binds
    @IntoMap
    @ViewModelKey(MovieListFragmentViewModel.class)
    abstract ViewModel bindMainFragmentViewModel(MainFragmentViewModel viewModel);
    */
}
