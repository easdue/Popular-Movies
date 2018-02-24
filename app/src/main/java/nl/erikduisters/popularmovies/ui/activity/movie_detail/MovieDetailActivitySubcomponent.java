package nl.erikduisters.popularmovies.ui.activity.movie_detail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.popularmovies.di.ActivityScope;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

@Subcomponent(modules = {MovieDetailActivityBindingModule.class})
@ActivityScope
public interface MovieDetailActivitySubcomponent extends AndroidInjector<MovieDetailActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MovieDetailActivity> {}
}