package popularmovies.theo.tziomakas.popularmovies.utitilities;

/**
 * Created by theodosiostziomakas on 27/01/2018.
 */

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import popularmovies.theo.tziomakas.popularmovies.BuildConfig;
import popularmovies.theo.tziomakas.popularmovies.DetailsActivity;
import popularmovies.theo.tziomakas.popularmovies.DetailsFragment;
import popularmovies.theo.tziomakas.popularmovies.adapters.ReviewsAdapter;
import popularmovies.theo.tziomakas.popularmovies.model.Reviews;



/**
 * A class that fetches reviews from the api
 */
public class FetchReviewsTask extends AsyncTask<Void,Void,ArrayList<Reviews>> {

    private static final String LOG_TAG = "FetchReviewsTask";
    private String mMovieId;
    private RecyclerView reviewReclerView;
    private TextView noReviewTextView;
    private ReviewsAdapter reviewsAdapter;


    public interface FetchReviewTaskCallback {
        void onReviewsReceived(ArrayList<Reviews> reviews);
    }

    FetchReviewTaskCallback callback;

    public FetchReviewsTask(FetchReviewTaskCallback callback,String mMovieId, RecyclerView reviewReclerView,
                            TextView noReviewTextView, ReviewsAdapter reviewsAdapter){
        this.callback = callback;
        this.mMovieId = mMovieId;
        this.reviewReclerView = reviewReclerView;
        this.noReviewTextView = noReviewTextView;
        this.reviewsAdapter = reviewsAdapter;
    }

    @Override
    protected ArrayList<Reviews> doInBackground(Void...voids) {

        String reviewsUrl = "https://api.themoviedb.org/3/movie/" + mMovieId + "/reviews" + "?api_key=" + BuildConfig.MOVIESDB_API_KEY;
        Log.v(LOG_TAG, "Inside doInBackground");
        ArrayList<Reviews> result = NetworkUtils.fetchReviewData(reviewsUrl);

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Reviews> reviewsList) {
        super.onPostExecute(reviewsList);
        Log.v(LOG_TAG, "Inside onPostExecute");

        if(reviewsList!=null && !reviewsList.isEmpty()){
            reviewsAdapter.setReviewsData(reviewsList);
        }else{
            showErrorMessage();
        }

        callback.onReviewsReceived(reviewsList);
    }

    public void showErrorMessage(){
        reviewReclerView.setVisibility(View.INVISIBLE);
        noReviewTextView.setVisibility(View.VISIBLE);
    }
}
