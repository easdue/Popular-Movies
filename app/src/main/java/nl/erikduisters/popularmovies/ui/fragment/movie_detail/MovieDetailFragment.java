package nl.erikduisters.popularmovies.ui.fragment.movie_detail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.local.MovieRepository;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.data.model.Status;
import nl.erikduisters.popularmovies.data.model.Video;
import nl.erikduisters.popularmovies.glide.GlideApp;
import nl.erikduisters.popularmovies.ui.BaseFragment;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentViewState.MovieViewState;
import nl.erikduisters.popularmovies.ui.fragment.movie_detail.MovieDetailFragmentViewState.TrailerViewState;

/**
 * Created by Erik Duisters on 21-02-2018.
 */

public class MovieDetailFragment extends BaseFragment<MovieDetailFragmentViewModel> implements TrailerAdapter.OnItemClickListener {
    private final static String KEY_MOVIE_ID = "MovieID";
    private final static String KEY_SCROLL_Y = "ScrollY";
    private final static String KEY_TRAILER_STATE = "TrailerState";

    @BindView(R.id.pageProgressGroup) LinearLayout pageProgressGroup;
    @BindView(R.id.pageProgressBar) ProgressBar pageProgressBar;
    @BindView(R.id.pageProgressMessage) TextView pageProgressMessage;
    @BindView(R.id.contentGroup) ScrollView contentGroup;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.favorite) ImageView favorite;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.releaseDate) TextView releaseDate;
    @BindView(R.id.voteAverage) TextView voteAverage;
    @BindView(R.id.numberOfVotes) TextView numberOfVotes;
    @BindView(R.id.popularity) TextView popularity;
    @BindView(R.id.overview) TextView overview;
    @BindView(R.id.trailers) RecyclerView trailersRecyclerView;
    @BindView(R.id.trailersProgressGroup) LinearLayout trailersProgressGroup;
    @BindView(R.id.trailersProgressBar) ProgressBar trailersProgressBar;
    @BindView(R.id.trailersProgressMessage) TextView trailersProgressMessage;

    private final TrailerAdapter trailerAdapter;
    private final LinearLayoutManager layoutManager;
    private Movie currentMovie;
    private Context context;
    private int scrollViewY;
    private Parcelable trailerLayoutManagerState;

    public static MovieDetailFragment newInstance(int movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_MOVIE_ID, movieId);

        fragment.setArguments(args);

        return fragment;
    }

    public MovieDetailFragment() {
        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setOnItemClickListener(this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        scrollViewY = -1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.getMovieViewState().observe(this, this::render);
        viewModel.getTrailerViewState().observe(this, this::render);
        viewModel.getStartActivityViewState().observe( this, this::render);
        viewModel.getToastViewState().observe(this, this::render);

        Bundle args = getArguments();

        viewModel.setMovieId(args == null ? MovieRepository.INVALID_MOVIE_ID :
                getArguments().getInt(KEY_MOVIE_ID, MovieRepository.INVALID_MOVIE_ID));

        if (savedInstanceState != null) {
            MovieViewState state = viewModel.getMovieViewState().getValue();

            if (state == null || state.status == Status.LOADING) {
                scrollViewY = savedInstanceState.getInt(KEY_SCROLL_Y);
                trailerLayoutManagerState = savedInstanceState.getParcelable(KEY_TRAILER_STATE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        trailersRecyclerView.setHasFixedSize(true);
        trailersRecyclerView.setLayoutManager(layoutManager);
        trailersRecyclerView.setAdapter(trailerAdapter);

        favorite.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SCROLL_Y, contentGroup.getScrollY());
        outState.putParcelable(KEY_TRAILER_STATE, layoutManager.onSaveInstanceState());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_movie_detail;
    }

    @Override
    protected Class<MovieDetailFragmentViewModel> getViewModelClass() {
        return MovieDetailFragmentViewModel.class;
    }

    private void render(@Nullable MovieViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState.status == Status.SUCCESS) {
            pageProgressGroup.setVisibility(View.INVISIBLE);
            contentGroup.setVisibility(View.VISIBLE);

            Movie movie = viewState.movie;

            if (movie != null) {
                currentMovie = movie;
                renderMovie(movie);
            }
        }

        if (viewState.status == Status.LOADING) {
            pageProgressGroup.setVisibility(View.VISIBLE);
            contentGroup.setVisibility(View.INVISIBLE);

            pageProgressBar.setVisibility(View.VISIBLE);
            pageProgressMessage.setText(getString(R.string.loading));
        }

        if (viewState.status == Status.ERROR) {
            pageProgressGroup.setVisibility(View.VISIBLE);
            contentGroup.setVisibility(View.INVISIBLE);

            pageProgressBar.setVisibility(View.INVISIBLE);
            pageProgressMessage.setText(getString(viewState.errorLabel, viewState.errorArgument));
        }
    }

    private void renderMovie(@NonNull Movie movie) {
        //TODO: listener and TextView to show loading failed
        GlideApp.with(context)
                .load(movie.getPosterPath())
                .into(poster);

        favorite.setImageResource(movie.isFavorite() ? R.drawable.vector_drawable_ic_favorite :
                R.drawable.vector_drawable_ic_favorite_outline);
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        voteAverage.setText(String.format(Locale.getDefault(),"%.1f", movie.getVoteAverage()));
        numberOfVotes.setText(String.valueOf(movie.getVoteCount()));
        popularity.setText(String.format(Locale.getDefault(), "%.2f", movie.getPopularity()));
        overview.setText(movie.getOverview());
    }

    private void render(@Nullable TrailerViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState.status == Status.SUCCESS) {
            trailerAdapter.setTrailerList(viewState.trailerList);
            trailersProgressGroup.setVisibility(View.GONE);

            if (scrollViewY != -1) {
                contentGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        contentGroup.scrollTo(0, scrollViewY);
                        scrollViewY = -1;
                    }
                });
            }

            if (trailerLayoutManagerState != null) {
                layoutManager.onRestoreInstanceState(trailerLayoutManagerState);
                trailerLayoutManagerState = null;
            }
        }

        if (viewState.status == Status.LOADING) {
            trailersProgressGroup.setVisibility(View.VISIBLE);
            trailersProgressBar.setVisibility(View.VISIBLE);
            trailersProgressMessage.setText(R.string.loading);
        }

        if (viewState.status == Status.ERROR) {
            trailersProgressGroup.setVisibility(View.VISIBLE);
            trailersProgressBar.setVisibility(View.GONE);
            trailersProgressMessage.setText(getString(viewState.errorLabel, viewState.errorArgument));
        }
    }

    private void render(@Nullable MovieDetailFragmentViewState.StartActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        try {
            startActivity(viewState.getIntent());
            viewModel.onActivityStarted();
        } catch (ActivityNotFoundException e1) {
            viewModel.onActivityStartFailed();
        }
    }

    private void render(@Nullable MovieDetailFragmentViewState.ToastViewState viewState) {
        if (viewState == null) {
            return;
        }

        Toast.makeText(getContext(), getString(viewState.message), viewState.displayShortMessage ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG)
                .show();

        viewModel.onToastDisplayed();
    }

    @Override
    public void onItemClick(Video video) {
        viewModel.onTrailerClicked(video);
    }

    @OnClick(R.id.favorite)
    public void onFavoriteClicked() {
        viewModel.onFavoriteClicked(currentMovie);
    }
}
