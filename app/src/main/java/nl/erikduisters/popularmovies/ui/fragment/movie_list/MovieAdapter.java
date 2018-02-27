package nl.erikduisters.popularmovies.ui.fragment.movie_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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

        return new MovieViewHolder(v);
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

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(Movie movie) {
            GlideApp.with(itemView.getContext())
                    .load(movie.getPosterPath())
                    .into(imageView);
        }
    }
}
