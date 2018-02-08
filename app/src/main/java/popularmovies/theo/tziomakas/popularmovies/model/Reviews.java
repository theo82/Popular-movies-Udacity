package popularmovies.theo.tziomakas.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by theodosiostziomakas on 26/01/2018.
 */

public class Reviews implements Parcelable{
    String movieId;
    String reviewId;
    String author;
    String content;
    String url;

    public Reviews(String movieId,String reviewId,String author,String content,String url){
        this.movieId = movieId;
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    protected Reviews(Parcel in) {
        movieId = in.readString();
        reviewId = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    public String getMovieId() {
        return movieId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(reviewId);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
}
