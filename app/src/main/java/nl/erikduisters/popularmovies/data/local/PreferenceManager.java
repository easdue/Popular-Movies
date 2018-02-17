package nl.erikduisters.popularmovies.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import nl.erikduisters.popularmovies.R;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

public class PreferenceManager {
    private final static String KEY_SORT_BY_HIGHEST_RATED = "sort_by_highest_rated";
    private final static String KEY_TMDB_CONFIGURATION = "tmdb_configuration";
    private final static String KEY_TMDB_CONFIGURATION_READ_DATE = "tmdb_configuration_read_date";

    private static PreferenceManager instance;
    private SharedPreferences sharedPreferences;

    private PreferenceManager(Context ctx) {
        android.preference.PreferenceManager.setDefaultValues(ctx, R.xml.preferences, true);
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static PreferenceManager instance(Context ctx) {
        if (instance == null) {
            instance = new PreferenceManager(ctx);
        }

        return instance;
    }

    public boolean getSortByHighestRated() {
        return sharedPreferences.getBoolean(KEY_SORT_BY_HIGHEST_RATED, false);
    }

    public void setSortByHighestRated(boolean sortByHighestRated) {
        sharedPreferences
                .edit()
                .putBoolean(KEY_SORT_BY_HIGHEST_RATED, sortByHighestRated)
                .apply();
    }
}
