package nl.erikduisters.popularmovies.ui.fragment.movie_reviews;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.model.Status;
import nl.erikduisters.popularmovies.ui.BaseFragment;

import static android.view.View.GONE;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

public class MovieReviewsFragment extends BaseFragment<MovieReviewsFragmentViewModel> {
    private static final String KEY_MOVIE_ID = "MovieId";
    private static final String KEY_LAYOUT_MANAGER_STATE = "LayoutManagerState";

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.progressMessage) TextView progressMessage;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private final ReviewAdapter reviewAdapter;
    private Parcelable layoutManagerState;

    public MovieReviewsFragment() {
        reviewAdapter = new ReviewAdapter();
    }

    public static MovieReviewsFragment newInstance(int movieId) {
        MovieReviewsFragment fragment = new MovieReviewsFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_MOVIE_ID, movieId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.getReviewsViewState().observe(this, this::render);

        Bundle args = getArguments();

        viewModel.setMovieId(args == null ? MovieRepository.INVALID_MOVIE_ID :
                getArguments().getInt(KEY_MOVIE_ID, MovieRepository.INVALID_MOVIE_ID));

        if (savedInstanceState != null) {
            MovieReviewsFragmentViewState viewState = viewModel.getReviewsViewState().getValue();
            if (viewState == null || viewState.status == Status.LOADING) {
                layoutManagerState = savedInstanceState.getParcelable(KEY_LAYOUT_MANAGER_STATE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(reviewAdapter);

        return v;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_movie_reviews;
    }

    @Override
    protected Class<MovieReviewsFragmentViewModel> getViewModelClass() {
        return MovieReviewsFragmentViewModel.class;
    }

    private void render(@Nullable MovieReviewsFragmentViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState.status == Status.LOADING) {
            progressBar.setVisibility(View.VISIBLE);
            progressMessage.setText(getString(R.string.loading));
            progressMessage.setVisibility(View.VISIBLE);
        }

        if (viewState.status == Status.ERROR) {
            progressBar.setVisibility(GONE);
            progressMessage.setText(getString(viewState.errorLabel, viewState.errorArgument));
            progressMessage.setVisibility(View.VISIBLE);
        }

        if (viewState.status == Status.SUCCESS) {
            if (viewState.emptyRevieListMessage != 0) {
                progressBar.setVisibility(GONE);
                progressMessage.setText(viewState.emptyRevieListMessage);
            } else {
                progressBar.setVisibility(GONE);
                progressMessage.setVisibility(GONE);
            }

            reviewAdapter.setReviewList(viewState.reviewList);

            if (layoutManagerState != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerState);
                layoutManagerState = null;
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_LAYOUT_MANAGER_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }
}
