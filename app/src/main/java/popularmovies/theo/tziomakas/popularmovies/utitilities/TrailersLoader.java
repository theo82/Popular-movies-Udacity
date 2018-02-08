package popularmovies.theo.tziomakas.popularmovies.utitilities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import popularmovies.theo.tziomakas.popularmovies.model.Movies;
import popularmovies.theo.tziomakas.popularmovies.model.Trailers;

/**
 * Created by theodosiostziomakas on 01/02/2018.
 */

public class TrailersLoader extends AsyncTaskLoader<List<Trailers>> {

    String url;

    public TrailersLoader(Context context,String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailers> loadInBackground() {
        if(url == null) {
            return null;
        }

        List<Trailers> result = NetworkUtils.fetchTrailersData(url);

        return result;
    }
}
