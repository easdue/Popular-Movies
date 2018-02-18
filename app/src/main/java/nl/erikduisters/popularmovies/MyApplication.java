package nl.erikduisters.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import nl.erikduisters.popularmovies.di.DaggerAppComponent;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 16-02-2018.
 */

public class MyApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        DaggerAppComponent.builder()
                .create(this)
                .inject(this);
    }

    private static class ReleaseTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        }
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
