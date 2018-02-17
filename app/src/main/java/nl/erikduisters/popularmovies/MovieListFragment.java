package nl.erikduisters.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import nl.erikduisters.popularmovies.data.local.PreferenceManager;

/**
 * Created by Erik Duisters on 16-02-2018.
 */

public class MovieListFragment extends Fragment {
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private Unbinder unbinder;

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
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);

        unbinder = ButterKnife.bind(this, v);

        progressBar.setVisibility(View.VISIBLE);
        textView.setText(R.string.loading);
        textView.setVisibility(View.VISIBLE);

        return v;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }

        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.movie_list_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean sortByHighestRated = PreferenceManager.instance(getContext()).getSortByHighestRated();

        menu.findItem(R.id.menu_highestRated).setChecked(sortByHighestRated);
        menu.findItem(R.id.menu_mostPopular).setChecked(!sortByHighestRated);
    }

    //TODO: Fetch the correctly sorted movie list from TMDB
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PreferenceManager preferenceManager = PreferenceManager.instance(getContext());

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
