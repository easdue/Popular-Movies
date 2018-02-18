package nl.erikduisters.popularmovies.ui.activity.main_activity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.popularmovies.di.ActivityScope;

/**
 * Created by Erik Duisters on 04-12-2017.
 */

@Subcomponent(modules = {MainActivityBindingModule.class})
@ActivityScope
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {}
}
