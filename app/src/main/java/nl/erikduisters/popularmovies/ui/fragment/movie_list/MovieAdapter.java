package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.Movie;
import nl.erikduisters.popularmovies.glide.GlideApp;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 20-02-2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> implements View.OnClickListener {
    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private List<Movie> movieList;
    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;
    private final MyRequestListener requestListener;

    public MovieAdapter() {
        requestListener = new MyRequestListener();
    }

    public void setMovieList(List<Movie> movielist) {
        this.movieList = movielist;

        //TODO: Use ListUtils to generate individual changed/removed calls
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        this.recyclerView = null;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_cell, parent, false);

        v.setOnClickListener(this);

        return new MovieViewHolder(v, requestListener);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildViewHolder(v).getAdapterPosition();

        Timber.d("onClick(), position: %d", position);

        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(movieList.get(position));
        }
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.errorMessage) TextView errorMessage;
        @BindView(R.id.progressBar) ProgressBar progressBar;
        @BindView(R.id.imageView) ImageView imageView;

        private final MyRequestListener requestListener;

        public MovieViewHolder(View itemView, MyRequestListener requestListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            this.requestListener = requestListener;
        }

        public void bind(Movie movie) {
            errorMessage.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            imageView.setContentDescription(movie.getTitle());

            GlideApp.with(itemView.getContext())
                    .load(movie.getPosterPath())
                    .listener(requestListener)
                    .override(Target.SIZE_ORIGINAL)
                    .into(imageView);
        }

        void hideProgressBar() {
            progressBar.setVisibility(View.GONE);
        }

        void showErrorMessage() {
            errorMessage.setText(R.string.loading_failed);
            errorMessage.setVisibility(View.VISIBLE);
        }
    }

    private class MyRequestListener implements RequestListener<Drawable> {
        private @Nullable MovieViewHolder getViewHolder(Target<Drawable> target) {
            ImageView imageView = ((DrawableImageViewTarget)target).getView();
            FrameLayout frameLayout = (FrameLayout) imageView.getParent();

            /* Both of the below methods sometimes return null
               recyclerView.findContainingViewHolder(frameLayout);
               recyclerView.findContainingItemView(((DrawableImageViewTarget) target).getView());
             */

            return (MovieViewHolder) recyclerView.getChildViewHolder(frameLayout);
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            MovieViewHolder vh = getViewHolder(target);

            if (vh != null) {
                vh.hideProgressBar();
                vh.showErrorMessage();
            }

            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            MovieViewHolder vh = getViewHolder(target);

            if (vh != null) {
                vh.hideProgressBar();
            }

            return false;
        }
    }
}
