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

import javax.inject.Inject;

import butterknife.BindView;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.PreferenceManager;
import nl.erikduisters.popularmovies.ui.BaseFragment;

/**
 * Created by Erik Duisters on 16-02-2018.
 */

public class MovieListFragment extends BaseFragment {
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Inject
    PreferenceManager preferenceManager;

    public MovieListFragment() {}

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);
        textView.setText(R.string.loading);
        textView.setVisibility(View.VISIBLE);

        return v;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_movie_list;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.movie_list_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean sortByHighestRated = preferenceManager.getSortByHighestRated();

        menu.findItem(R.id.menu_highestRated).setChecked(sortByHighestRated);
        menu.findItem(R.id.menu_mostPopular).setChecked(!sortByHighestRated);
    }

    //TODO: Fetch the correctly sorted movie list from TMDB
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_highestRated:
                preferenceManager.setSortByHighestRated(true);
                invalidateOptionsMenu();
                break;
            case R.id.menu_mostPopular:
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
