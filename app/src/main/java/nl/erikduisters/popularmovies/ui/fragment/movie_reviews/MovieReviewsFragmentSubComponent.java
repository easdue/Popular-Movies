package nl.erikduisters.popularmovies.ui.fragment.movie_reviews;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.popularmovies.di.FragmentScope;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

@FragmentScope
@Subcomponent
public interface MovieReviewsFragmentSubComponent extends AndroidInjector<MovieReviewsFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MovieReviewsFragment> {}
}
