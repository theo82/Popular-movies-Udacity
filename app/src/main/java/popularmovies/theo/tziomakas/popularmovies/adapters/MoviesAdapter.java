package popularmovies.theo.tziomakas.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;
import popularmovies.theo.tziomakas.popularmovies.R;
import popularmovies.theo.tziomakas.popularmovies.model.Movies;
import popularmovies.theo.tziomakas.popularmovies.utitilities.NetworkUtils;

/**
 * Created by theodosiostziomakas on 29/12/2017.
 */

            public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>{

                private Context context;
                private List<Movies> moviesList;


                final private MoviesAdapterOnClickHandler mClickHandler;

                public interface MoviesAdapterOnClickHandler{
                    void onClick(String movieId);
                }


                public MoviesAdapter(Context context, List<Movies> moviesList, MoviesAdapterOnClickHandler mClickHandler ){
                    this.context = context;
                    this.moviesList = moviesList;
                    this.mClickHandler = mClickHandler;
                }


                @Override
                public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    Context context = parent.getContext();
                    int layoutIdForListItem = R.layout.movies_row;
                    LayoutInflater inflater = LayoutInflater.from(context);

                    boolean shouldAttactToParentImmediately = false;

                    View view = inflater.inflate(layoutIdForListItem,parent,shouldAttactToParentImmediately);

                    MoviesViewHolder moviesViewHolder = new MoviesViewHolder(view);

                    return moviesViewHolder;
                }

                @Override
                public void onBindViewHolder(MoviesViewHolder holder, int position) {

                    Movies movies = moviesList.get(position);


                    Picasso.with(context)
                            .load(NetworkUtils.getImageURL(movies.getImageThumbnail()))
                            .placeholder(R.drawable.ic_broken_image)
                            .into(holder.mMovieImage);

                    Log.v("MovieAdapter",NetworkUtils.getImageURL(movies.getImageThumbnail()));

                }



                @Override
                public int getItemCount() {
                    return moviesList.size();
                }

                public void setMoviesData(List<Movies> moviesList){
                    this.moviesList = moviesList;
                    notifyDataSetChanged();
                }

                public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

                    ImageView mMovieImage;

                    public MoviesViewHolder(View itemView) {
                        super(itemView);

                        mMovieImage = itemView.findViewById(R.id.movieImage);
                        itemView.setOnClickListener(this);
                    }


                    @Override
                    public void onClick(View v) {
                        int adapterPosition = getAdapterPosition();
                        String movieId = moviesList.get(adapterPosition).getMovieId();
                        Log.v("MovieAdapter",movieId);
                        mClickHandler.onClick(movieId);
                    }
                }
            }
