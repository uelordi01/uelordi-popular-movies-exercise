package udacity.uelordi.com.popularmovies.content;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import udacity.uelordi.com.popularmovies.database.MovieContract;

/**
 * Created by uelordi on 28/02/2017.
 */

public class MovieContentDetails implements Parcelable {
    private final String IMAGE_URL_PATH = "http://image.tmdb.org/t/p/w185";

    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    protected MovieContentDetails(Parcel in) {
        //IMAGE_URL_PATH = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(IMAGE_URL_PATH);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieContentDetails> CREATOR = new Creator<MovieContentDetails>() {
        @Override
        public MovieContentDetails createFromParcel(Parcel in) {
            return new MovieContentDetails(in);
        }

        @Override
        public MovieContentDetails[] newArray(int size) {
            return new MovieContentDetails[size];
        }
    };

}



