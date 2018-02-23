package nl.erikduisters.popularmovies.ui.fragment.movie_detail;

import android.location.GnssClock;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.Status;
import nl.erikduisters.popularmovies.glide.GlideApp;
import nl.erikduisters.popularmovies.ui.BaseFragment;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

public class MovieDetailFragment extends BaseFragment<MovieDetailFragmentViewModel> {
    final static String KEY_MOVIE_ID = "MovieID";

    @BindView(R.id.progressGroup) LinearLayout progressGroup;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.progressMessage) TextView progressMessage;
    @BindView(R.id.contentGroup) ScrollView contentGroup;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.releaseDate) TextView releaseDate;
    @BindView(R.id.voteAverage) TextView voteAverage;
    @BindView(R.id.numberOfVotes) TextView numberOfVotes;
    @BindView(R.id.popularity) TextView popularity;
    @BindView(R.id.overview) TextView overview;

    public static MovieDetailFragment newInstance(int movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_MOVIE_ID, movieId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.getViewState().observe(this, this::render);

        Bundle args = getArguments();

        viewModel.setMovieId(args == null ? MovieRepository.INVALID_MOVIE_ID :
                getArguments().getInt(KEY_MOVIE_ID, MovieRepository.INVALID_MOVIE_ID));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frament_movie_detail;
    }

    @Override
    protected Class<MovieDetailFragmentViewModel> getViewModelClass() {
        return MovieDetailFragmentViewModel.class;
    }

    private void render(@Nullable MovieDetailFragmentViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState.status == Status.SUCCESS) {
            progressGroup.setVisibility(View.INVISIBLE);
            contentGroup.setVisibility(View.VISIBLE);

            Movie movie = viewState.movie;

            GlideApp.with(getContext())
                    .load("https://image.tmdb.org/t/p/w500/" + viewState.movie.getPosterPath())
                    .into(poster);

            title.setText(movie.getTitle());
            releaseDate.setText(movie.getReleaseDate());
            voteAverage.setText(String.format(Locale.getDefault(),"%.1f", movie.getVoteAverage()));
            numberOfVotes.setText(String.valueOf(movie.getVoteCount()));
            popularity.setText(String.format(Locale.getDefault(), "%.2f", movie.getPopularity()));
            overview.setText(movie.getOverview());
        }

        if (viewState.status == Status.LOADING) {
            progressGroup.setVisibility(View.VISIBLE);
            contentGroup.setVisibility(View.INVISIBLE);

            progressBar.setVisibility(View.VISIBLE);
            progressMessage.setText(getString(R.string.loading));
        }

        if (viewState.status == Status.ERROR) {
            progressGroup.setVisibility(View.VISIBLE);
            contentGroup.setVisibility(View.INVISIBLE);

            progressBar.setVisibility(View.VISIBLE);
            progressMessage.setText(getString(viewState.errorLabel, viewState.errorArgument));
        }
    }
}
