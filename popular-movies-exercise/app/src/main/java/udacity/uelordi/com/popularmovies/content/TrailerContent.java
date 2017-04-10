package udacity.uelordi.com.popularmovies.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by uelordi on 28/02/2017.
 */

public class TrailerContent implements Parcelable
{
  private String videoId;
  private String videoKey;
  private String videoName;

  protected TrailerContent(Parcel in) {
    videoId = in.readString();
    videoKey = in.readString();
    videoName = in.readString();
    posterPath = in.readString();
  }

  public static final Creator<TrailerContent> CREATOR = new Creator<TrailerContent>() {
    @Override
    public TrailerContent createFromParcel(Parcel in) {
      return new TrailerContent(in);
    }

    @Override
    public TrailerContent[] newArray(int size) {
      return new TrailerContent[size];
    }
  };

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  private String posterPath;
  private final String YOUTUBE_BASE_URL="http://www.youtube.com/watch?v=";

  public TrailerContent(String movieID, String videoKey, String trailerName)
  {
    videoId=movieID;
    this.videoKey=videoKey;
    videoName=trailerName;
  }

  public String getVideoId() {
    return videoId;
  }

  public void setVideoId(String videoId) {
    this.videoId = videoId;
  }

  public String getVideoKey() {
    return videoKey;
  }

  public void setVideoKey(String videoKey) {
    this.videoKey = videoKey;
  }

  public String getVideoName() {
    return videoName;
  }

  public void setVideoName(String videoName) {
    this.videoName = videoName;
  }

  public String getTrailerURL() {
    return YOUTUBE_BASE_URL  + videoKey;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(videoId);
    dest.writeString(videoKey);
    dest.writeString(videoName);
    dest.writeString(posterPath);
    dest.writeString(YOUTUBE_BASE_URL);
  }
}
