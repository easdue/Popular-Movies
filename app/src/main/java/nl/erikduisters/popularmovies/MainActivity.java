package nl.erikduisters.popularmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

//TODO: Make sure app does not crash when there is no internet connection
public class MainActivity extends AppCompatActivity {
    private static final String TAG_MOVIE_LIST_FRAGMENT = "MovieListFragment";

    @BindView(R.id.toolbar) Toolbar toolbar;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

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
    protected void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }
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
