package nl.erikduisters.popularmovies.ui.activity.main_activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.Unbinder;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.ui.BaseActivity;
import nl.erikduisters.popularmovies.ui.fragment.movie_list.MovieListFragment;
import timber.log.Timber;

//TODO: Make sure app does not crash when there is no internet connection
public class MainActivity extends BaseActivity {
    private static final String TAG_MOVIE_LIST_FRAGMENT = "MovieListFragment";

    @BindView(R.id.toolbar) Toolbar toolbar;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_MOVIE_LIST_FRAGMENT);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentPlaceholder, MovieListFragment.newInstance(), TAG_MOVIE_LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                //TODO: Show about dialog that gives credit to TMDB
                Timber.d("About was clicked");
        }

        return super.onOptionsItemSelected(item);
    }
}
