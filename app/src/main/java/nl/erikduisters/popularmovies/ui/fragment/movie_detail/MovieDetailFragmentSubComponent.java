package nl.erikduisters.popularmovies.ui.fragment.movie_detail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.popularmovies.di.FragmentScope;

/**
 * Created by Erik Duisters on 21-02-2018.
 */
@FragmentScope
@Subcomponent
public interface MovieDetailFragmentSubComponent extends AndroidInjector<MovieDetailFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MovieDetailFragment> {}
}
