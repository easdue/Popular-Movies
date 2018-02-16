package nl.erikduisters.popularmovies;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import timber.log.Timber;

/**
 * Created by Erik Duisters on 16-02-2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        android.preference.PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, true);
    }

    private static class ReleaseTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
        }
    }
}
