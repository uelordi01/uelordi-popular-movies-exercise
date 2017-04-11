package udacity.uelordi.com.popularmovies.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by uelordi on 22/03/17.
 */

public class MovieContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "udacity.uelordi.com.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String PATH_MOVIES = "favorite_movies";


    public static final class MovieEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_SYNOPSYS = "synopsys";
        public static final String COLUMN_IMAGE_URL = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public final static String SQL_CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_SYNOPSYS + " TEXT NOT NULL, " +
                COLUMN_USER_RATING +  "  REAL NOT NULL," +
                COLUMN_IMAGE_URL   + " TEXT NOT NULL,"+
                COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "+
                COLUMN_RELEASE_DATE + " TEXT NOT NULL)";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static final String[] MOVIE_COLUMNS = {
                _ID,
                COLUMN_TITLE,
                COLUMN_SYNOPSYS,
                COLUMN_USER_RATING,
                COLUMN_IMAGE_URL,
                COLUMN_RELEASE_DATE,
                COLUMN_BACKDROP_PATH
        };

        public static final int COL_MOVIE_ID = 0;
        public static final int COL_MOVIE_TITLE = 1;
        public static final int COL_MOVIE_OVERVIEW = 2;
        public static final int COL_MOVIE_VOTE_AVERAGE = 3;
        public static final int COL_MOVIE_POSTER_PATH = 4;
        public static final int COL_MOVIE_BACKDROP_PATH = 5;
        public static final int COL_MOVIE_RELEASE_DATE = 6;

        }
}
