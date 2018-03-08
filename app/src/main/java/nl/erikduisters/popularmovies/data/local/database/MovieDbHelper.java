package nl.erikduisters.popularmovies.data.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nl.erikduisters.popularmovies.data.local.database.MovieContract.MovieEntry;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    MovieDbHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                        MovieEntry.COLUMN_POPULARITY + " REAL, " +
                        MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        MovieEntry.COLUMN_TITLE + " TEXT, " +
                        MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                        MovieEntry.COLUMN_VOTE_COUNT + " INTEGER);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
