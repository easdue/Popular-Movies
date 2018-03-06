package nl.erikduisters.popularmovies.ui.activity.movie_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.ui.BaseActivity;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

public class MovieDetailActivity extends BaseActivity<MovieDetailActivityViewModel> implements ViewPager.OnPageChangeListener {
    public static final String KEY_MOVIE_ID = "MovieID";
    private static final String TAG_MOVIE_DETAIL_FRAGMENT = "MovieDetailFragment";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tablayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int movieId = getIntent().getIntExtra(KEY_MOVIE_ID, MovieRepository.INVALID_MOVIE_ID);


        MovieDetailActivityPagerAdapter adapter =
                new MovieDetailActivityPagerAdapter(getSupportFragmentManager(), movieId, getBaseContext());
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
