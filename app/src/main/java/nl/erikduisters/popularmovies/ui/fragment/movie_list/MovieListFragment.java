package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
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

import butterknife.BindView;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.Status;
import nl.erikduisters.popularmovies.ui.BaseFragment;
import nl.erikduisters.popularmovies.util.MenuUtil;
import nl.erikduisters.popularmovies.util.MyMenuItem;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 16-02-2018.
 */

public class MovieListFragment extends BaseFragment<MovieListFragmentViewModel> implements MovieAdapter.OnItemClickListener {
    private final static String KEY_LAYOUTMANAGER_STATE = "LayoutManagerState";

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private List<MyMenuItem> optionsMenu;
    private MovieAdapter movieAdapter;
    private GridLayoutManager layoutManager;

    public MovieListFragment() {}

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    private Parcelable layoutManagerState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        optionsMenu = new ArrayList<>();

        viewModel.getMovieViewState().observe(this, this::render);
        viewModel.getStartActivityViewState().observe(this, this::render);

        movieAdapter = new MovieAdapter();
        movieAdapter.setOnItemClickListener(this);

        layoutManagerState = null;

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LAYOUTMANAGER_STATE)) {
            MovieListFragmentViewState.MovieViewState state = viewModel.getMovieViewState().getValue();

            if (state == null || state.status == Status.LOADING) {
                layoutManagerState = savedInstanceState.getParcelable(KEY_LAYOUTMANAGER_STATE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        movieAdapter.setOnItemClickListener(null);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_movie_list;
    }

    @Override
    protected Class<MovieListFragmentViewModel> getViewModelClass() {
        return MovieListFragmentViewModel.class;
    }

    private void render(@Nullable MovieListFragmentViewState.MovieViewState viewState) {
        if (viewState == null) {
            return;
        }

        optionsMenu = viewState.optionsMenu;
        invalidateOptionsMenu();

        if (viewState.status == Status.SUCCESS) {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);

            layoutManager.setSpanCount(viewModel.getSpanCount());
            movieAdapter.setMovieList(viewState.movieList);

            if (viewState.emptyMovieListText != 0) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(viewState.emptyMovieListText);
            }

            if (layoutManagerState != null) {
                layoutManager.onRestoreInstanceState(layoutManagerState);
                layoutManagerState = null;
            }
        }

        if (viewState.status == Status.LOADING) {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.loading);
        }

        if (viewState.status == Status.ERROR) {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(getString(viewState.errorLabel, viewState.errorArgument));
        }
    }

    private void render(@Nullable MovieListFragmentViewState.StartActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        startActivity(viewState.getIntent(getContext()));

        viewModel.onActivityStarted();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.menu_highestRated:
            case R.id.menu_mostPopular:
            case R.id.menu_favorites:
                viewModel.onMenuItemClicked(item.getItemId());
                return true;
            default:
                break;
        }

        return false;
    }

    private void invalidateOptionsMenu() {
        FragmentActivity activity = getActivity();

        if (activity != null) {
            activity.invalidateOptionsMenu();
        }
    }

    @Override
    public void onItemClick(Movie movie) {
        viewModel.onMovieClicked(movie);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_LAYOUTMANAGER_STATE, layoutManager.onSaveInstanceState());
    }
}
