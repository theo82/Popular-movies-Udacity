        package popularmovies.theo.tziomakas.popularmovies;


        import android.content.ActivityNotFoundException;
        import android.content.AsyncQueryHandler;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Color;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Parcelable;
        import android.provider.UserDictionary;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.LoaderManager;
        import android.support.v4.app.ShareCompat;
        import android.support.v4.content.AsyncTaskLoader;
        import android.support.v4.content.CursorLoader;
        import android.support.v4.content.Loader;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.ToggleButton;

        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;
        import java.util.List;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.Unbinder;
        import popularmovies.theo.tziomakas.popularmovies.adapters.MyDividerItemDecoration;
        import popularmovies.theo.tziomakas.popularmovies.adapters.ReviewsAdapter;
        import popularmovies.theo.tziomakas.popularmovies.adapters.TrailersAdapter;
        import popularmovies.theo.tziomakas.popularmovies.data.FavouriteContract;
        import popularmovies.theo.tziomakas.popularmovies.data.SettingsActivity;
        import popularmovies.theo.tziomakas.popularmovies.model.Movies;
        import popularmovies.theo.tziomakas.popularmovies.model.Reviews;
        import popularmovies.theo.tziomakas.popularmovies.model.Trailers;
        import popularmovies.theo.tziomakas.popularmovies.utitilities.ApiClient;
        import popularmovies.theo.tziomakas.popularmovies.utitilities.ApiInterface;
        import popularmovies.theo.tziomakas.popularmovies.utitilities.FetchReviewsTask;
        import popularmovies.theo.tziomakas.popularmovies.utitilities.NetworkUtils;
        import popularmovies.theo.tziomakas.popularmovies.utitilities.ReviewsLoader;
        import popularmovies.theo.tziomakas.popularmovies.utitilities.TrailersLoader;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;


        /**
         * A simple {@link Fragment} subclass.
         */
         public class DetailsFragment extends Fragment implements
                            LoaderManager.LoaderCallbacks<Object>,
                            TrailersAdapter.TrailersAdapterOnClickHandler{
         private static final int TRAILERS_LOADER_ID = 0;
         private static final int REVIEWS_LOADER_ID =   1;

            private static final String TRAILERS_LAYOUT = "DetailsFragment.trailer.layout";
            private static final String REVIEWS_LAYOUT = "DetailsFragment.review.layout";

            int recyclerViewOrientation = LinearLayoutManager.VERTICAL;

            private static final String TRAILERS_LIST = "trailers_list";
            private static final String REVIEWS_LIST = "reviews_list";
            /*****************************************************
             * Lists where fetched trailers and reviews are stored.
             *****************************************************/
            private List<Trailers> trailersList = new ArrayList<>();
            private List<Reviews> reviewsList = new ArrayList<>();

            private static final String[] FAVOURITE_MOVIE_PROJECTION = {
                                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID,
                                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_POSTER_PATH,
                                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_TITLE,
                                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW,
                                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                                FavouriteContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE
                        };


            Unbinder unbinder;


            /**************************************************************************
             * Those custom adapter will actually send the views to the recycler views.
             **************************************************************************/
            private  TrailersAdapter trailersAdapter;
            private  ReviewsAdapter reviewsAdapter;

            /*******************************************
             * RecyclerViews that will display our data.
             *******************************************/
            private  RecyclerView trailersRecyclerView;
            private  RecyclerView reviewsRecyclerView;


            private static final String MOVIE_SHARE_HASHTAG = " #PopularMoviesApp";
            private static final String LOG_TAG = "DetailsFragment";

            private Cursor favoriteCursor;
            private String mMovieId;
            private String mImage;
            private String mTitle;
            private double mRating;
            private String mDate;
            private String mOverview;
            private String oldReviewText;

            @BindView(R.id.no_reviews_text_view)
            public TextView noReviewTextView;

            @BindView(R.id.no_trailers_text_view)
            public TextView noTrailersTextView;

            @BindView(R.id.movie_image)
            public ImageView i;

            @BindView(R.id.movie_title)
            public TextView t;

            @BindView(R.id.movie_rating)
            public TextView r;

            @BindView(R.id.movie_date)
            public TextView d;

            @BindView(R.id.movie_overview)
            public TextView o;

            private ToggleButton b;
            private Uri uri;
            public DetailsFragment() {
                // Required empty public constructor
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setHasOptionsMenu(true);
            }

            @Override
            public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);

                if (savedInstanceState != null) {
                    if (savedInstanceState.containsKey("title")
                        && savedInstanceState.containsKey("rating")
                        && savedInstanceState.containsKey("date")
                        && savedInstanceState.containsKey("overview")
                        && savedInstanceState.containsKey("image"))
                         {

                        String oldTitle = savedInstanceState.getString("title");
                        String oldRating = savedInstanceState.getString("rating");
                        String oldDate = savedInstanceState.getString("date");
                        String oldOverview = savedInstanceState.getString("overview");
                        String oldImage = savedInstanceState.getString("image");

                        t.setText(oldTitle);
                        r.setText(oldRating);
                        d.setText(oldDate);
                        o.setText(oldOverview);
                        Picasso.with(getContext()).load(NetworkUtils.IMAGE_URL + NetworkUtils.IMAGE_SIZE_185 + oldImage).into(i);

                    }
                }
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     final Bundle savedInstanceState) {
                // Inflate the layout for this fragment
                View root = inflater.inflate(R.layout.fragment_details, container, false);

                unbinder = ButterKnife.bind(this, root);
                Intent intent = getActivity().getIntent();
                mMovieId = intent.getStringExtra("movieId");

                /*
                mImage = intent.getStringExtra("image");
                mTitle = intent.getStringExtra("title");
                mRating = intent.getStringExtra("rating");
                mDate = intent.getStringExtra("date");
                mOverview = intent.getStringExtra("overview");
                */


                b = root.findViewById(R.id.add_favourite);

                /*******************
                 *      Movies     *
                 *******************/

                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);

                Call<Movies> call = apiService.getMovieDetails(mMovieId, BuildConfig.MOVIESDB_API_KEY);
                call.enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {
                        Movies movie = response.body();
                        mImage = movie.getImageThumbnail();
                        mTitle = movie.getTitle();
                        mOverview = movie.getOverview();
                        mRating = movie.getRating();
                        mDate = movie.getDate();

                        Picasso.with(getContext()).load(NetworkUtils.IMAGE_URL + NetworkUtils.IMAGE_SIZE_185 + mImage).into(i);

                        t.setText(mTitle);
                        o.setText(mOverview);
                        r.setText(String.valueOf(mRating));
                        d.setText(mDate);

                    }

                    @Override
                    public void onFailure(Call<Movies> call, Throwable t) {
                        // Log error here since request failed
                        Log.e(LOG_TAG, t.toString());
                    }
                });
                /*******************
                 *    Trailers     *
                 *******************/
                getActivity().getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, null, this);

                if(savedInstanceState == null) {
                    trailersRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_trailers);
                /*
                 * A LinearLayoutManager is responsible for measuring and positioning item views within a
                 * RecyclerView into a linear list. This means that it can produce either a horizontal or
                 * vertical list depending on which parameter you pass in to the LinearLayoutManager
                 * constructor. In our case, we want a vertical list, so we pass in the constant from the
                 * LinearLayoutManager class for vertical lists, LinearLayoutManager.VERTICAL.
                 *
                 * There are other LayoutManagers available to display your data in uniform grids,
                 * staggered grids, and more! See the developer documentation for more details.
                 */


                 /*
                 *  This value should be true if you want to reverse your layout. Generally, this is only
                 *  true with horizontal lists that need to support a right-to-left layout.
                 */
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    trailersRecyclerView.setLayoutManager(mLayoutManager);
                    trailersRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), recyclerViewOrientation, 16));

                /*
                 * Use this setting to improve performance if you know that changes in content do not
                 * change the child layout size in the RecyclerView
                 */
                    trailersRecyclerView.setHasFixedSize(true);

                    trailersAdapter = new TrailersAdapter(trailersList, this);

                    trailersRecyclerView.setAdapter(trailersAdapter);
                }else{


                    trailersList = savedInstanceState.getParcelableArrayList(REVIEWS_LIST);
                    trailersRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_trailers);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    trailersRecyclerView.setLayoutManager(mLayoutManager);
                    trailersRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), recyclerViewOrientation, 16));

                /*
                 * Use this setting to improve performance if you know that changes in content do not
                 * change the child layout size in the RecyclerView
                 */
                    trailersRecyclerView.setHasFixedSize(true);

                    trailersAdapter = new TrailersAdapter(trailersList, this);

                    trailersRecyclerView.setAdapter(trailersAdapter);

                }
                /*******************
                 *     Reviews     *
                 *******************/

                getActivity().getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, null, this);
                if(savedInstanceState == null) {
                    reviewsRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_reviews);
                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
                    int recyclerViewOrientation = LinearLayoutManager.VERTICAL;
                    reviewsRecyclerView.setLayoutManager(mLayoutManager1);
                    reviewsRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), recyclerViewOrientation, 16));

                    reviewsAdapter = new ReviewsAdapter(reviewsList);
                    reviewsRecyclerView.setAdapter(reviewsAdapter);

                /*
                if(savedInstanceState == null) {
                    reviewsRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_reviews);
                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());

                    reviewsRecyclerView.setLayoutManager(mLayoutManager1);
                    reviewsRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), recyclerViewOrientation, 16));

                    reviewsAdapter = new ReviewsAdapter(reviewsList);
                    reviewsRecyclerView.setAdapter(reviewsAdapter);

                    //new FetchReviewsTask(this,mMovieId,reviewsRecyclerView,noReviewTextView,reviewsAdapter).execute();
                }
                */
                    /****************************************************************
                     *     If phone is rotated read the stored review list values   *
                     *     and reconstruct the recycler view                        *
                     ****************************************************************/


                }else{
                    reviewsList = savedInstanceState.getParcelableArrayList(REVIEWS_LIST);

                    reviewsRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_reviews);
                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
                    int recyclerViewOrientation = LinearLayoutManager.VERTICAL;
                    reviewsRecyclerView.setLayoutManager(mLayoutManager1);
                    reviewsRecyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), recyclerViewOrientation, 16));

                    reviewsAdapter = new ReviewsAdapter(reviewsList);
                    reviewsRecyclerView.setAdapter(reviewsAdapter);

                }



                b.setText(null);
                b.setTextOn(null);
                b.setTextOff(null);


                /********************************************************************************
                 * Everytime a open this fragment, I check if the particular movie is in the db.
                 * If yes, the star button is checked and when pressed again it is deleted from
                 * the favourites db. If not it means that it is not in the db. So when the star
                 * button is click, the movie will be added as favourite.
                 *******************************************************************************/


                favoriteCursor = getContext().getContentResolver().query(FavouriteContract.FavouriteEntry.CONTENT_URI,
                        null,
                        FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{mMovieId},
                        null);


                if (favoriteCursor.getCount() > 0) {
                    try {
                        b.setChecked(true);
                    }finally {
                        favoriteCursor.close();
                    }
                }

                b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                        if (isChecked) {
                            Log.v(LOG_TAG, String.valueOf(isChecked));
                            /************************************************
                             * An AsyncTask for adding a movie as favourite *
                             ***********************************************/

                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_ID, mMovieId);
                                    contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_POSTER_PATH, mImage);
                                    contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_TITLE, mTitle);
                                    contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW, mOverview);
                                    contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE, mDate);
                                    contentValues.put(FavouriteContract.FavouriteEntry.COLUMN_MOVIE_VOTE_AVERAGE, mRating);
                                    //The actual insertion in the db.
                                    uri = getActivity().getContentResolver().insert(FavouriteContract.FavouriteEntry.CONTENT_URI, contentValues);
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    if (uri != null)
                                    {
                                        Toast.makeText(getActivity(), "Movie:  " + mTitle + " was added", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }.execute();
                        } else {


                            Uri moviedIdOfFavMovie = FavouriteContract.FavouriteEntry.buildMovieUriWithId(mMovieId);

                            getActivity().getContentResolver().delete(
                                    moviedIdOfFavMovie,
                                    null,
                                    null);
                            Toast.makeText(getActivity(), "Movie deleted from favourites ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                return root;
            }


            public void showTrailersErrorMessage() {
                                        /* First, hide the currently visible data */
                trailersRecyclerView.setVisibility(View.INVISIBLE);
                                        /* Then, show the error */
                noTrailersTextView.setVisibility(View.VISIBLE);
            }

            public void showErrorMessage(){
                reviewsRecyclerView.setVisibility(View.INVISIBLE);
                noReviewTextView.setVisibility(View.VISIBLE);
            }
            /**
             * Uses the ShareCompat Intent builder to create our Forecast intent for sharing. We set the
             * type of content that we are sharing (just regular text), the text itself, and we return the
             * newly created Intent.
             *
             * @return The Intent to use to start our share.
             */
            private Intent createShareMovieIntent() {
                Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(mTitle + MOVIE_SHARE_HASHTAG + "\n\n\n" + mOverview)
                        .getIntent();
                return shareIntent;
            }

            @Override
            public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
                inflater.inflate(R.menu.detail, menu);
                /* Return true so that the menu is displayed in the Toolbar */
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                /* Get the ID of the clicked item */
                int id = item.getItemId();

                /* Share menu item clicked */
                if (id == R.id.action_share) {
                    Intent shareIntent = createShareMovieIntent();
                    startActivity(shareIntent);
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }

            /*
            @Override
            public Loader<List<Trailers>> onCreateLoader(int id, Bundle args) {

                String trailersUrl = "https://api.themoviedb.org/3/movie/" + mMovieId + "/videos" + "?api_key=" + BuildConfig.MOVIESDB_API_KEY;

                return new TrailersLoader(getActivity(),trailersUrl);
            }
            */

            @Override
            public Loader onCreateLoader(int id, Bundle args) {
                if (id == REVIEWS_LOADER_ID){

                    String reviewsUrl = "https://api.themoviedb.org/3/movie/" + mMovieId + "/reviews" + "?api_key=" + BuildConfig.MOVIESDB_API_KEY;

                    return new ReviewsLoader(getActivity(),reviewsUrl);
                }else if (id == TRAILERS_LOADER_ID) {

                    String trailersUrl = "https://api.themoviedb.org/3/movie/" + mMovieId + "/videos" + "?api_key=" + BuildConfig.MOVIESDB_API_KEY;

                    return new TrailersLoader(getActivity(),trailersUrl);
                }

                return null;
            }

            @Override
            public void onLoadFinished(Loader<Object> loader, Object data) {

                int id = loader.getId();

                if (id == REVIEWS_LOADER_ID) {

                    reviewsList = (List<Reviews>) data;

                    if (reviewsList != null && !reviewsList.isEmpty()) {
                        reviewsAdapter.setReviewsData(reviewsList);
                    } else {
                        showErrorMessage();
                    }
                } else if (id == TRAILERS_LOADER_ID) {


                    if (id == TRAILERS_LOADER_ID) {

                        trailersList = (List<Trailers>) data;

                        if (trailersList != null && !trailersList.isEmpty()) {
                            trailersAdapter.setTrailersData(trailersList);
                        } else {
                            showTrailersErrorMessage();
                        }
                    }

                }


            }

            @Override
            public void onLoaderReset(Loader<Object> loader) {

            }

            /**
             * A method I override from the
             * @param trailerKey The key of the youtube movie
             */
            @Override
            public void onClick(String trailerKey) {
                //Toast.makeText(getActivity(),trailerKey,Toast.LENGTH_LONG).show();
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));
                try {
                    getActivity().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    getActivity().startActivity(webIntent);
                }
            }


            @Override
            public void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);

                    String currentTitle = t.getText().toString();
                    String currentRating = r.getText().toString();
                    String currentDate = d.getText().toString();
                    String currentOverview = o.getText().toString();



                    outState.putString("title", currentTitle);
                    outState.putString("rating", String.valueOf(currentRating));
                    outState.putString("date", currentDate);
                    outState.putString("overview", currentOverview);
                    outState.putString("image", mImage);


                    outState.putParcelable(REVIEWS_LAYOUT,reviewsRecyclerView.getLayoutManager().onSaveInstanceState());
                    outState.putParcelable(TRAILERS_LAYOUT,trailersRecyclerView.getLayoutManager().onSaveInstanceState());

                    outState.putParcelableArrayList(REVIEWS_LIST, (ArrayList<? extends Parcelable>) reviewsList);
                    outState.putParcelableArrayList(TRAILERS_LIST, (ArrayList<? extends Parcelable>) trailersList);

                //outState.putParcelableArrayList(REVIEW_LIST, (ArrayList<? extends Parcelable>) reviewsList);


            }

            @Override
            public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
                super.onViewStateRestored(savedInstanceState);

                if(savedInstanceState != null){
                    Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(REVIEWS_LAYOUT);
                    reviewsRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

                    Parcelable savedRecyclerLayoutState1 = savedInstanceState.getParcelable(TRAILERS_LAYOUT);
                    trailersRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState1);
                }
            }

            /*
            @Override
            public void onReviewsReceived(ArrayList<Reviews> reviews) {
                reviewsList = reviews;
            }
            */

            @Override
            public void onDestroy() {
                super.onDestroy();
                unbinder.unbind();
            }
        }
