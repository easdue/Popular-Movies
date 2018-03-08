package nl.erikduisters.popularmovies.data.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Erik Duisters on 08-03-2018.
 */
@IntDef({SortOrder.POPULARITY, SortOrder.TOP_RATED, SortOrder.FAVORITE})
@Retention(RetentionPolicy.SOURCE)
public @interface SortOrder {
    int POPULARITY = 0;
    int TOP_RATED = 1;
    int FAVORITE = 2;
}
