package nl.erikduisters.popularmovies.ui.activity.movie_detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragment;
import nl.erikduisters.popularmovies.ui.fragment.movie_reviews.MovieReviewsFragment;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

public class MovieDetailActivityPagerAdapter extends FragmentPagerAdapter {
    private final int movieId;
    private final Context ctx;

    public MovieDetailActivityPagerAdapter(FragmentManager fm, int movieId, Context ctx) {
        super(fm);

        this.movieId = movieId;
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MovieDetailFragment.newInstance(movieId);
            case 1:
                return MovieReviewsFragment.newInstance(movieId);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ctx.getString(R.string.details);
            case 1:
                return ctx.getString(R.string.reviews);
        }

        return null;
    }
}
