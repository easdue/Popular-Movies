package nl.erikduisters.popularmovies.ui.fragment.movie_detail;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import nl.erikduisters.popularmovies.data.model.Video;
import nl.erikduisters.popularmovies.glide.GlideApp;
import nl.erikduisters.popularmovies.util.ViewHolderClickListener;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 03-03-2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> implements ViewHolderClickListener<TrailerAdapter.TrailerViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Video video);
    }

    private final GlideRequestListener glideRequestListener;
    private List<Video> trailerList;
    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;

    TrailerAdapter() { glideRequestListener = new GlideRequestListener(); }

    void setTrailerList(List<Video> trailerList) {
        this.trailerList = trailerList;

        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer, parent, false);

        return new TrailerViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(trailerList.get(position), glideRequestListener);
    }

    @Override
    public int getItemCount() {
        return trailerList == null ? 0 : trailerList.size();
    }

    @Override
    public void onViewRecycled(TrailerViewHolder holder) {
        super.onViewRecycled(holder);

        Timber.e("onViewRecycled");
        GlideApp.with(holder.itemView.getContext()).clear(holder.trailerImage);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        this.recyclerView = null;
    }

    @Override
    public void onClick(TrailerViewHolder viewHolder) {
        if (onItemClickListener != null) {
            int position = viewHolder.getAdapterPosition();

            onItemClickListener.onItemClick(trailerList.get(position));
        }
    }

    static class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailerImage) ImageView trailerImage;
        @BindView(R.id.progressBar) ProgressBar progressBar;
        @BindView(R.id.errorMessage) TextView errorMessage;
        @BindView(R.id.play) ImageView playButton;

        @NonNull private final ViewHolderClickListener<TrailerViewHolder> listener;

        TrailerViewHolder(View itemView, @NonNull ViewHolderClickListener<TrailerViewHolder> listener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            this.listener = listener;

            playButton.setOnClickListener(this);
        }

        void bind(Video video, GlideRequestListener requestListener) {
            progressBar.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
            trailerImage.setContentDescription(video.getName());

            GlideApp.with(itemView.getContext())
                    .load(video.getThumbnailUrl())
                    .listener(requestListener)
                    .override(Target.SIZE_ORIGINAL)
                    .into(trailerImage);
        }

        void loadingFailed() {
            progressBar.setVisibility(View.INVISIBLE);
            errorMessage.setText(R.string.loading_failed);
            errorMessage.setVisibility(View.VISIBLE);
        }

        void hideProgressBar() {
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(this);
        }
    }

    private class GlideRequestListener implements RequestListener<Drawable> {
        private @Nullable
        TrailerViewHolder getViewHolder(Target<Drawable> target) {
            ImageView imageView = ((DrawableImageViewTarget)target).getView();
            ConstraintLayout constraintLayout = (ConstraintLayout) imageView.getParent();

            return (TrailerViewHolder) recyclerView.getChildViewHolder(constraintLayout);
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            if (recyclerView.isComputingLayout()) {
                recyclerView.post(() -> onLoadFailed(e, model, target, isFirstResource));

                return false;
            }

            TrailerViewHolder vh = getViewHolder(target);

            if (vh != null) {
                vh.loadingFailed();
            }

            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            if (recyclerView.isComputingLayout()) {
                recyclerView.post(() -> onResourceReady(resource, model, target, dataSource, isFirstResource));

                return false;
            }

            TrailerViewHolder vh = getViewHolder(target);

            if (vh != null) {
                vh.hideProgressBar();
            }

            return false;
        }
    }
}
