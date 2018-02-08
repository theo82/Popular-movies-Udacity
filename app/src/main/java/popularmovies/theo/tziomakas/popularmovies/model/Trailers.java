package popularmovies.theo.tziomakas.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by theodosiostziomakas on 26/01/2018.
 */

public class Trailers implements Parcelable{
    List<Trailers> trailers;


    public static Creator<Trailers> getCREATOR() {
        return CREATOR;
    }

    private String movieId;
    private String trailerId;
    private String key;
    private String trailerName;
    private String site;
    private String trailer;
    private String size;

    public Trailers(String movieId, String trailerId, String key, String name, String site, String size){
        this.movieId = movieId;
        this.trailerId = trailerId;
        this.key = key;
        this.trailerName = name;
        this.site = site;
        this.size = size;
    }

    protected Trailers(Parcel in) {
        movieId = in.readString();
        trailerId = in.readString();
        trailers = in.createTypedArrayList(Trailers.CREATOR);
        key = in.readString();
        trailerName = in.readString();
        site = in.readString();
        trailer = in.readString();
        size = in.readString();
    }

    public static final Creator<Trailers> CREATOR = new Creator<Trailers>() {
        @Override
        public Trailers createFromParcel(Parcel in) {
            return new Trailers(in);
        }

        @Override
        public Trailers[] newArray(int size) {
            return new Trailers[size];
        }
    };

    public List<Trailers> getTrailers() {
        return trailers;
    }

    public String getKey() {
        return key;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getSite() {
        return site;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getSize() {
        return size;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTrailerId() {
        return trailerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(trailerId);
        dest.writeTypedList(trailers);
        dest.writeString(key);
        dest.writeString(trailerName);
        dest.writeString(site);
        dest.writeString(trailer);
        dest.writeString(size);
    }
}
