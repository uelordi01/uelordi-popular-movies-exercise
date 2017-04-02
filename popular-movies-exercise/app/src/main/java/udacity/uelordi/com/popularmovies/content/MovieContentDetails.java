package udacity.uelordi.com.popularmovies.content;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import udacity.uelordi.com.popularmovies.database.MovieContract;

/**
 * Created by uelordi on 28/02/2017.
 */

public class MovieContentDetails implements Parcelable
{

    private String poster_path;
    private final String IMAGE_URL_PATH="http://image.tmdb.org/t/p/w185/";
    private String title;
    private String synopsis;
    private String user_rating;
    private String release_date;
    private long movieID;
    //TODO MAKE ADD MOVIE AND ADD REVIEW:
    List<ReviewContent> reviewContent= new ArrayList<>();
    List<TrailerContent> trailerContent= new ArrayList<>();

    protected MovieContentDetails(Parcel in) {
        poster_path = in.readString();
        title = in.readString();
        synopsis = in.readString();
        user_rating = in.readString();
        release_date = in.readString();
        movieID = in.readLong();
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

    public void addReview(String author, String content)
    {
        reviewContent.add(new ReviewContent(author,content));
    }
    public void addTrailer(String movieID, String videoKey, String trailerName)
    {
        trailerContent.add(new TrailerContent(movieID,videoKey,trailerName));
    }
    public List<ReviewContent> getReviewContent() {
        return reviewContent;
    }

    public List<TrailerContent> getTrailerContent() {
        return trailerContent;
    }

    public long getMovieID() {
        return movieID;
    }

    public void setMovieID(long movieID) {
        this.movieID = movieID;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = IMAGE_URL_PATH+poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID,movieID);
        values.put(MovieContract.MovieEntry.COLUMN_IMAGE_URL,poster_path);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE,title);
        values.put(MovieContract.MovieEntry.COLUMN_SYNOPSYS,synopsis);
        values.put(MovieContract.MovieEntry.COLUMN_USER_RATING,user_rating);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,release_date);
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(IMAGE_URL_PATH);
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeString(user_rating);
        dest.writeString(release_date);
        dest.writeLong(movieID);
    }
}
