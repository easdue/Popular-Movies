package nl.erikduisters.popularmovies.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.Configuration;
import nl.erikduisters.popularmovies.di.ApplicationContext;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

@Singleton
public class PreferenceManager {
    private final static String KEY_SORT_BY_HIGHEST_RATED = "sort_by_highest_rated";
    private final static String KEY_TMDB_CONFIGURATION = "tmdb_configuration";
    private final static String KEY_TMDB_CONFIGURATION_READ_DATE = "tmdb_configuration_read_date";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Inject
    PreferenceManager(@ApplicationContext Context ctx, Gson gson) {
        android.preference.PreferenceManager.setDefaultValues(ctx, R.xml.preferences, true);
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        this.gson = gson;
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

    public @Nullable Configuration getTMDBConfiguration() {
        String json = sharedPreferences.getString(KEY_TMDB_CONFIGURATION, "");

        return gson.fromJson(json, Configuration.class);
    }

    public void setTMDBConfiguration(Configuration configuration) {
        sharedPreferences
                .edit()
                .putLong(KEY_TMDB_CONFIGURATION_READ_DATE, Calendar.getInstance().getTimeInMillis())
                .putString(KEY_TMDB_CONFIGURATION, gson.toJson(configuration))
                .apply();
    }

    public Date getTMDBConfigurationReadDate() {
        return new Date(sharedPreferences.getLong(KEY_TMDB_CONFIGURATION_READ_DATE, 0));
    }
}
