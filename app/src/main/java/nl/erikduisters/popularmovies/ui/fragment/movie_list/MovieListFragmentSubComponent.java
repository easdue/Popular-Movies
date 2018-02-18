package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.popularmovies.di.FragmentScope;

/**
 * Created by Erik Duisters on 18-02-2018.
 */

@FragmentScope
@Subcomponent
public interface MovieListFragmentSubComponent extends AndroidInjector<MovieListFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MovieListFragment> {}
}
