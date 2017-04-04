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

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_POPULAR = "most_popular";
    public static final String PATH_RATED = "highest_rated";
    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_MOVIES = "movies";
    public static final String COLUMN_MOVIE_ID_FKEY = "movie_id";

    public static final class MovieEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_SYNOPSYS = "synopsys";
        public static final String COLUMN_IMAGE_URL = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public final static String SQL_CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_SYNOPSYS + " TEXT NOT NULL, " +
                COLUMN_USER_RATING +  "  REAL NOT NULL," +
                COLUMN_IMAGE_URL   + " TEXT NOT NULL,"+
                COLUMN_RELEASE_DATE + " TEXT NOT NULL)";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
        //public static final String COLUMN_RELEASE_DATE = "title";

        }
    public static final class PopularEntry implements BaseColumns {
        public static final Uri CONTENT_URI =  MovieEntry.CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES
                        + "/" + PATH_POPULAR;

        public static final String TABLE_NAME = "most_popular";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MOVIE_ID_FKEY + " INTEGER NOT NULL, " +

                        " FOREIGN KEY (" + COLUMN_MOVIE_ID_FKEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " +

                        " );";
        }
    public static final class HighestRatedEntry implements BaseColumns {
        public static final Uri CONTENT_URI =  MovieEntry.CONTENT_URI.buildUpon().appendPath(PATH_RATED).build();


        public static final String TABLE_NAME = "highest_rated";
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES
                        + "/" + PATH_RATED;

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MOVIE_ID_FKEY + " INTEGER NOT NULL, " +

                        " FOREIGN KEY (" + COLUMN_MOVIE_ID_FKEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " +

                        " );";
    }
    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =  MovieEntry.CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();


        public static final String TABLE_NAME = "favorites";
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES
                        + "/" + PATH_FAVORITES;

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MOVIE_ID_FKEY + " INTEGER NOT NULL, " +

                        " FOREIGN KEY (" + COLUMN_MOVIE_ID_FKEY + ") REFERENCES " +
                        MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " +

                        " );";
    }



}
