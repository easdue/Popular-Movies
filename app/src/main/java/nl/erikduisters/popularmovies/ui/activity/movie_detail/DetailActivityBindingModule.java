package nl.erikduisters.popularmovies.ui.activity.movie_detail;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragment;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentSubComponent;

/**
 * Created by Erik Duisters on 21-02-2018.
 */
@Module(subcomponents = {MovieDetailFragmentSubComponent.class})
abstract class DetailActivityBindingModule {
    @Binds
    @IntoMap
    @FragmentKey(MovieDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindDetailActivityFragmentInjectorFactory(MovieDetailFragmentSubComponent.Builder builder);
}
