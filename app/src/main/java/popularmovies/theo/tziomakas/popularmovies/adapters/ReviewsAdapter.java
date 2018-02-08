package popularmovies.theo.tziomakas.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import popularmovies.theo.tziomakas.popularmovies.R;
import popularmovies.theo.tziomakas.popularmovies.model.Reviews;

/**
 * Created by theodosiostziomakas on 27/01/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>{

    private List<Reviews> reviewsList;

    public ReviewsAdapter(List<Reviews> reviewsList){
        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.reviews_row;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttactToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttactToParentImmediately);

        ReviewsViewHolder reviewsViewHolder = new ReviewsViewHolder(view);

        return reviewsViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {

        Reviews reviews = reviewsList.get(position);
        holder.mReviewTextView.setText(reviews.getContent());


    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public void setReviewsData(List<Reviews> reviewsList){
        this.reviewsList = reviewsList;
        notifyDataSetChanged();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView mReviewTextView;

        public ReviewsViewHolder(View itemView) {
            super(itemView);

            mReviewTextView = (TextView)itemView.findViewById(R.id.review_text_view);
        }
    }


}
