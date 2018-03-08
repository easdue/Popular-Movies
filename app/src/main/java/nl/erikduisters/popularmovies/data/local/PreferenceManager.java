package nl.erikduisters.popularmovies.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.Configuration;
import nl.erikduisters.popularmovies.data.model.SortOrder;
import nl.erikduisters.popularmovies.di.ApplicationContext;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

@Singleton
public class PreferenceManager {
    private final String KEY_SORT_ORDER;
    private final String KEY_TMDB_CONFIGURATION;
    private final String KEY_TMDB_CONFIGURATION_READ_DATE;

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    PreferenceManager(@ApplicationContext Context ctx, Gson gson) {
        android.preference.PreferenceManager.setDefaultValues(ctx, R.xml.preferences, true);
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        this.gson = gson;

        KEY_SORT_ORDER = ctx.getString(R.string.prefs_sort_order);
        KEY_TMDB_CONFIGURATION = ctx.getString(R.string.prefs_tmdb_configuration);
        KEY_TMDB_CONFIGURATION_READ_DATE = ctx.getString(R.string.prefs_tmdb_configuration_read_date);
    }

    public @SortOrder int getSortOrder() {
        return sharedPreferences.getInt(KEY_SORT_ORDER, SortOrder.POPULARITY);
    }

    public void setSortOrder(@SortOrder int sortOrder) {
        sharedPreferences
                .edit()
                .putInt(KEY_SORT_ORDER, sortOrder)
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
