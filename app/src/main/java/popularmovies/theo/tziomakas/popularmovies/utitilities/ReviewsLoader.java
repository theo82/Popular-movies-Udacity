package popularmovies.theo.tziomakas.popularmovies.utitilities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import popularmovies.theo.tziomakas.popularmovies.model.Reviews;
import popularmovies.theo.tziomakas.popularmovies.model.Trailers;

/**
 * Created by theodosiostziomakas on 06/02/2018.
 */

public class ReviewsLoader extends AsyncTaskLoader<List<Reviews>> {

    String url;

    public ReviewsLoader(Context context,String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Reviews> loadInBackground() {
        if(url == null) {
            return null;
        }

        List<Reviews> result = NetworkUtils.fetchReviewData(url);

        return result;
    }
}
