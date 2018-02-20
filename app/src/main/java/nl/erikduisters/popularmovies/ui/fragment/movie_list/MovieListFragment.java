package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.local.PreferenceManager;
import nl.erikduisters.popularmovies.ui.BaseFragment;
import nl.erikduisters.popularmovies.util.MenuUtil;
import nl.erikduisters.popularmovies.util.MyMenuItem;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 16-02-2018.
 */

public class MovieListFragment extends BaseFragment<MovieListFragmentViewModel> {
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    MovieRepository movieRepository;

    private List<MyMenuItem> optionsMenu;

    public MovieListFragment() {}

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        optionsMenu = new ArrayList<>();

        viewModel.getViewState().observe(this, this::render);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_movie_list;
    }

    @Override
    protected Class<MovieListFragmentViewModel> getViewModelClass() {
        return MovieListFragmentViewModel.class;
    }

    private void render(@Nullable MovieListFragmentViewState viewState) {
        if (viewState == null) {
            return;
        }

        optionsMenu = viewState.optionsMenu;
        invalidateOptionsMenu();

        if (viewState.status == MovieListFragmentViewState.Status.SUCCESS) {
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);

            //TODO: set adapter data
        }

        if (viewState.status == MovieListFragmentViewState.Status.LOADING) {
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.loading);
        }

        if (viewState.status == MovieListFragmentViewState.Status.ERROR) {
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(getString(viewState.errorLabel, viewState.errorArgument));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.movie_list_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuUtil.updateMenu(menu, optionsMenu);
    }

    //TODO: Fetch the correctly sorted movie list from TMDB
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.menu_highestRated:
                //TODO: Move this to the ViewModel
                preferenceManager.setSortByHighestRated(true);
                invalidateOptionsMenu();
                break;
            case R.id.menu_mostPopular:
                //TODO: Move this to the ViewModel
                preferenceManager.setSortByHighestRated(false);
                invalidateOptionsMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void invalidateOptionsMenu() {
        FragmentActivity activity = getActivity();

        if (activity != null) {
            activity.invalidateOptionsMenu();
        }
    }
}
