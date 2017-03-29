package udacity.uelordi.com.popularmovies.content;

/**
 * Created by uelordi on 28/02/2017.
 */

public class TrailerContent
{
  private String videoId;
  private String videoKey;
  private String videoName;

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  private String posterPath;
  private final String YOUTUBE_BASE_URL="http://www.youtube.com";

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
    return YOUTUBE_BASE_URL + "/" + videoKey;
  }
}
