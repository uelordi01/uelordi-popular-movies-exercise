package udacity.uelordi.com.popularmovies.content;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by uelordi on 28/02/2017.
 */

public class ReviewContent implements Parcelable
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("url")
    @Expose
    private String url;

    public ReviewContent(String author, String content, String reviewUrl)
    {
        this.author = author;
        this.content = content;
        this.url = reviewUrl;
    }

    protected ReviewContent(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<ReviewContent> CREATOR = new Creator<ReviewContent>() {
        @Override
        public ReviewContent createFromParcel(Parcel in) {
            return new ReviewContent(in);
        }

        @Override
        public ReviewContent[] newArray(int size) {
            return new ReviewContent[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getReviewUrl() {
        return url;
    }

    public void setReviewUrl(String reviewUrl) {
        this.url = reviewUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);

    }
}
