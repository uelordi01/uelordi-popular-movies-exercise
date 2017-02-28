package udacity.uelordi.com.popularmovies.content;

/**
 * Created by uelordi on 28/02/2017.
 */

public class MovieContent
{
    int id;
    String poster_path;
    final String IMAGE_URL_PATH="http://image.tmdb.org/t/p/w185/";
    String title;
    String synopsis;
    int user_rating;
    String release_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(int user_rating) {
        this.user_rating = user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
