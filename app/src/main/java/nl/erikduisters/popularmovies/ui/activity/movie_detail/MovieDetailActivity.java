package nl.erikduisters.popularmovies.ui.activity.movie_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.ui.BaseActivity;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragment;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

public class MovieDetailActivity extends BaseActivity<MovieDetailActivityViewModel> {
    public static final String KEY_MOVIE_ID = "MovieID";
    private static final String TAG_MOVIE_DETAIL_FRAGMENT = "MovieDetailFragment";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_MOVIE_DETAIL_FRAGMENT);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentPlaceholder, MovieDetailFragment
                            .newInstance(getIntent().getIntExtra(KEY_MOVIE_ID, MovieRepository.INVALID_MOVIE_ID)), TAG_MOVIE_DETAIL_FRAGMENT)
                    .commit();
        }

        viewModel.getViewState().observe(this, this::render);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected Class<MovieDetailActivityViewModel> getViewModelClass() {
        return MovieDetailActivityViewModel.class;
    }

    private void render(@Nullable MovieDetailActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState instanceof MovieDetailActivityViewState.FinishViewState) {
            NavUtils.navigateUpFromSameTask(this);
            viewModel.onFinished();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                viewModel.onHomeAsUpPressed();
                return true;
        }

        return false;
    }
}
