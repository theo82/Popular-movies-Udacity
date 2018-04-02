package popularmovies.theo.tziomakas.popularmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.content.CursorLoader;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import popularmovies.theo.tziomakas.popularmovies.adapters.MoviesAdapter;
import popularmovies.theo.tziomakas.popularmovies.data.FavouriteContract;
import popularmovies.theo.tziomakas.popularmovies.model.Movies;
import popularmovies.theo.tziomakas.popularmovies.utitilities.MovieLoader;


/**
 * A simple {@link Fragment} subclass. The logic of this fragment is as follows. I implement
 * an Loader that handles a generic Object. I use two loaders. One for fetching data from net and
 * and other that queries data from the db.
 */
public class MainActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Object>, MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String LOG_TAG = "MainActivityFragment";
    private static final String BUNDLE_GRID_LAYOUT = "grid_view";


    /***************************************************************
     * Loader constants that will be use in loaders initialization *
     ***************************************************************/
    private static final int MOVIES_LOADER_ID = 0;
    private static final int FAV_MOVIES_ID = 1;
    private static final String ARRAY_LIST = "list_view";
    private static final String LAYOUT_STATE = "MainActivityFragment.recycler.layout";

    private String moviesUrl;

    /******************************************************
     * Our UI elements the MoviesAdapter and the GridView *
     ******************************************************/
    private MoviesAdapter adapter;


    private RecyclerView recyclerView;

    /****************************************************
     * This will be used for the adapter initialization *
     ****************************************************/


    private ArrayList<Movies> moviesArrayList;

    private  TextView mErrorMessageDisplay;



    private  ProgressBar mLoadingIndicator;

    /********************************************************************************
     * This SharePreferences class is used to get the values of the ListPreferences *
     *******************************************************************************/
    private SharedPreferences sharedPrefs;
    private String sortingCriteria;

    /**********************************************
     * The column of the database we are querying *
     *********************************************/
    private static final String[] FAVOURITE_MOVIE_PROJECTION = {
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_POSTER_PATH,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_TITLE,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            FavouriteContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE
    };

    public MainActivityFragment() {
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState == null) {

             /* This TextView is used to display errors and will be hidden if there are no errors */
            mErrorMessageDisplay = rootView.findViewById(R.id.tv_error_message_display);

            moviesArrayList = new ArrayList<>();

            recyclerView = rootView.findViewById(R.id.movies_grid_view);


            adapter = new MoviesAdapter(getActivity(), moviesArrayList, this);


            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

            recyclerView.setLayoutManager(gridLayoutManager);


        /*

        * The ProgressBar that will indicate to the user that we are loading data. It will be
        * hidden when no data is loading.
        *
        * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
        * circle. We didn't make the rules (or the names of Views), we just follow them.
        */
            mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);

            recyclerView.setAdapter(adapter);


        }else{
            moviesArrayList = savedInstanceState.getParcelableArrayList(ARRAY_LIST);

            recyclerView = rootView.findViewById(R.id.movies_grid_view);


            adapter = new MoviesAdapter(getActivity(), moviesArrayList, this);


            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

            recyclerView.setLayoutManager(gridLayoutManager);


        /*

        * The ProgressBar that will indicate to the user that we are loading data. It will be
        * hidden when no data is loading.
        *
        * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
        * circle. We didn't make the rules (or the names of Views), we just follow them.
        */
            mLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);

            recyclerView.setAdapter(adapter);
        }




        return rootView;
    }

    /**
     * This method will make the error message visible and hide the movie
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    public  void showErrorMessage() {
                                /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
                                /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }



    @Override
    public void onResume() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortingCriteria = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_default_value));

        if (sortingCriteria.equals("popular") || sortingCriteria.equals("top_rated")) {

            Log.v(LOG_TAG,sortingCriteria);

            getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            getLoaderManager().destroyLoader(FAV_MOVIES_ID);

        }else {

            Log.v(LOG_TAG,sortingCriteria);
            adapter.clear();
            getLoaderManager().restartLoader(FAV_MOVIES_ID, null, this);
            getLoaderManager().destroyLoader(MOVIES_LOADER_ID);
        }

        super.onResume();

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        /*******************************************************************
         *       If fetching data from the net then call MovieLoader       *
         *******************************************************************/

        Log.v(LOG_TAG, String.valueOf(id));


        if (id == MOVIES_LOADER_ID) {
            //mLoadingIndicator.setVisibility(View.VISIBLE);

            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sortingCriteria = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_default_value));

            if (sortingCriteria.equals("popular") || sortingCriteria.equals("top_rated")) {

                moviesUrl = "https://api.themoviedb.org/3/movie/" + sortingCriteria + "?api_key=" + BuildConfig.MOVIESDB_API_KEY;
            }

            /*******************************************************************
             *   Call the MovieLoader class which contains an AsyncTaskLoader  *
             *******************************************************************/

            return new MovieLoader(getActivity(), sortingCriteria, moviesUrl);

        }

        /***********************************
         *   Else query data from the db   *
         ***********************************/
        else if (id == FAV_MOVIES_ID) {

            Uri movieUri = FavouriteContract.FavouriteEntry.CONTENT_URI;

            return new CursorLoader(
                    getActivity(),
                    movieUri,
                    FAVOURITE_MOVIE_PROJECTION,
                    null,
                    null,
                    null
            );
        }

        return null;

    }

    /*********************************************************************
     *   Basically here we are displaying the results of our operations  *
     *********************************************************************/
    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        int id = loader.getId();
        //Log.v(LOG_TAG, String.valueOf(id));
        if(id == MOVIES_LOADER_ID){

            // Cast Object to display a list of movies from the net.
            moviesArrayList = (ArrayList<Movies>)data;

            if(moviesArrayList != null && !moviesArrayList.isEmpty()) {
                adapter.setMoviesData(moviesArrayList);
            }else {
                showErrorMessage();
            }


        } else if(id == FAV_MOVIES_ID){
            // Cast Object to display data read from the db.
            Cursor dataCursor = (Cursor) data;

            //mLoadingIndicator.setVisibility(View.INVISIBLE);

            List<Movies> movies = new ArrayList<>();

            if(dataCursor != null && dataCursor.getCount()>0){

                while (dataCursor.moveToNext()) {
                    String cursorId = dataCursor.getString(0);
                    String title = dataCursor.getString(2);
                    String path = dataCursor.getString(1);
                    String overview = dataCursor.getString(3);
                    double rating = dataCursor.getDouble(4);
                    String date = dataCursor.getString(5);

                    Movies movie = new Movies(cursorId,title,path,overview,rating,date);
                    movies.add(movie);
                }
                adapter.setMoviesData(movies);

            }else{
                //recyclerView.setVisibility(View.INVISIBLE);
                //getLoaderManager().destroyLoader(MOVIES_LOADER_ID);
                adapter.clear();

                Toast.makeText(getActivity(),"You haven't added a movie as favourite",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(ARRAY_LIST,moviesArrayList);
        outState.putParcelable(LAYOUT_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }
    @Override
    public void onClick(String position) {
        Intent i = new Intent(getActivity(),DetailsActivity.class);
        i.putExtra("movieId",position);
        startActivity(i);

    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(LAYOUT_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }


}
