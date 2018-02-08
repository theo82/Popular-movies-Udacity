package popularmovies.theo.tziomakas.popularmovies.utitilities;

/**
 * Created by theodosiostziomakas on 02/01/2018.
 */


import popularmovies.theo.tziomakas.popularmovies.model.Movies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("movie/{id}")
    Call<Movies> getMovieDetails(@Path("id") String id, @Query("api_key") String apiKey);

}
