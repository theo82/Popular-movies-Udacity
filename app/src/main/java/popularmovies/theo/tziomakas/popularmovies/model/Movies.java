package popularmovies.theo.tziomakas.popularmovies.model;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by theodosiostziomakas on 29/12/2017.
 */

public class Movies implements Parcelable {
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    private List<Movie> results;
    private  String movieId;
    @SerializedName("title")
    private  String title;
    @SerializedName("poster_path")
    private  String imageThumbnail;
    @SerializedName("overview")
    private  String overview;
    @SerializedName("vote_average")
    private  double rating;

    public String getDate() {
        return date;
    }

    @SerializedName("release_date")
    private  String date;

    public String getTitle() {
        return title;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }


    public Movies(){

    }

    public Movies(String movieId,String title, String imageThumbnail,String overview,double rating,String date){
        this.movieId = movieId;
        this.title = title;
        this.imageThumbnail = imageThumbnail;
        this.overview = overview;
        this.rating = rating;
        this.date = date;
    }

    protected Movies(Parcel in) {
        this.movieId = in.readString();
        this.title = in.readString();
        this.imageThumbnail = in.readString();
        this.overview = in.readString();
        this.rating = in.readDouble();
        this.date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieId);
        parcel.writeString(title);
        parcel.writeString(imageThumbnail);
        parcel.writeString(overview);
        parcel.writeDouble(rating);
        parcel.writeString(date);
    }


    /**
     * An interface that generates instances of the Parceable class from a parcel so it is going
     * to unwrap it and read any set of properties from your creator class. The parcel is of type
     * {@link Movies}
     */
    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {

            return new Movies(in);

        }

        @Override
        public Movies[] newArray(int size) {

            return new Movies[size];

        }
    };

    public List<Movie> getResults() {
        return results;
    }

}

