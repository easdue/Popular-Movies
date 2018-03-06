package nl.erikduisters.popularmovies.ui.activity.movie_detail;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragment;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentSubComponent;
import nl.erikduisters.popularmovies.ui.fragment.movie_reviews.MovieReviewsFragment;
import nl.erikduisters.popularmovies.ui.fragment.movie_reviews.MovieReviewsFragmentSubComponent;

/**
 * Created by Erik Duisters on 21-02-2018.
 */
@Module(subcomponents = {MovieDetailFragmentSubComponent.class, MovieReviewsFragmentSubComponent.class})
abstract class MovieDetailActivityBindingModule {
    @Binds
    @IntoMap
    @FragmentKey(MovieDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindMovieDetailActivityFragmentInjectorFactory(MovieDetailFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @FragmentKey(MovieReviewsFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindMovieReviewsFFragmentInjectorFactory(MovieReviewsFragmentSubComponent.Builder builder);
}
