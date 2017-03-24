package udacity.uelordi.com.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by uelordi on 22/03/17.
 */

public class MovieContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "udacity.uelordi.com.popularmovies.database";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_MOVIES = "movies";


    public static final class MovieEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USER_RATING = "user_Rating";
        public static final String COLUMN_SYNOPSYS = "synopsys";
        public static final String COLUMN_IMAGE_URL = "poster_path";
        //public static final String COLUMN_RELEASE_DATE = "title";

    }
}
