package popularmovies.theo.tziomakas.popularmovies.utitilities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;
import popularmovies.theo.tziomakas.popularmovies.model.Movies;

/**
 * Created by theodosiostziomakas on 31/01/2018.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movies>> {

    private static final String LOG_TAG = MovieLoader.class.getName();

    String sortingCriteria;

    String url;

    public MovieLoader(Context context,String sortingCriteria,String url) {
        super(context);
        this.sortingCriteria = sortingCriteria;
        this.url = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movies> loadInBackground() {

        Log.v(LOG_TAG,"MovieLoader loadInBackground");

        if(url == null){
            return null;
        }

        List<Movies> result = NetworkUtils.fetchMoviesData(url);

        return result;
    }
}
