package udacity.uelordi.com.popularmovies.content;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by uelordi on 28/02/2017.
 */

public class TrailerContent implements Parcelable {

  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("iso_639_1")
  @Expose
  private String iso6391;
  @SerializedName("iso_3166_1")
  @Expose
  private String iso31661;
  @SerializedName("key")
  @Expose
  private String key;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("site")
  @Expose
  private String site;
  @SerializedName("size")
  @Expose
  private Integer size;
  @SerializedName("type")
  @Expose
  private String type;

  protected TrailerContent(Parcel in) {
    id = in.readString();
    iso6391 = in.readString();
    iso31661 = in.readString();
    key = in.readString();
    name = in.readString();
    site = in.readString();
    type = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(iso6391);
    dest.writeString(iso31661);
    dest.writeString(key);
    dest.writeString(name);
    dest.writeString(site);
    dest.writeString(type);
  }

  @Override
  public int describeContents() {
    return 0;
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
}

