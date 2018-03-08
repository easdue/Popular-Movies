package nl.erikduisters.popularmovies.ui.fragment.movie_reviews;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.erikduisters.popularmovies.R;
import nl.erikduisters.popularmovies.data.model.Review;

/**
 * Created by Erik Duisters on 06-03-2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> reviewList;

    void setReviewList(@NonNull List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(reviewList.get(position), position % 2 == 0);
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.constraintLayout) ConstraintLayout constraintLayout;
        @BindView(R.id.author) TextView author;
        @BindView(R.id.content) TextView content;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(Review review, boolean isEven) {
            constraintLayout.setBackgroundResource(isEven ? R.drawable.even_review_background : R.drawable.odd_review_background);
            author.setText(review.getAuthor());
            content.setText(review.getContent());
        }
    }
}
