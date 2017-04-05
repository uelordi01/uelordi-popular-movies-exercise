package udacity.uelordi.com.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import static udacity.uelordi.com.popularmovies.database.MovieContract.MovieEntry.TABLE_NAME;
/**
 * Created by uelordi on 22/03/17.
 */

public class MoviesProvider extends ContentProvider {
    private MovieDBHelper mMovieHelper;

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final int MOST_POPULAR = 202;
    public static final int  HIGHEST_RATED = 203;
    public static final int FAVORITES = 300;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final String SQL_INSERT_ERROR = "Failed to insert the row";

    private static final String MOVIE_ID_SELECTION =
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID + " = ? ";


   public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.AUTHORITY;

        uriMatcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        uriMatcher.addURI(authority, MovieContract.PATH_MOVIES + "/" +
                MovieContract.PATH_POPULAR, MOST_POPULAR);
        uriMatcher.addURI(authority, MovieContract.PATH_MOVIES + "/" +
                MovieContract.PATH_RATED, HIGHEST_RATED);

        uriMatcher.addURI(authority, MovieContract.PATH_MOVIES + "/" +
                MovieContract.PATH_FAVORITES, FAVORITES);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieHelper = new MovieDBHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
       final SQLiteDatabase db = mMovieHelper.getReadableDatabase();
       int match = sUriMatcher.match(uri);
       Cursor retCursor = null;
       switch (match)
       {
           case MOVIES:
           {
               retCursor = mMovieHelper.getReadableDatabase().query(
                       MovieContract.MovieEntry.TABLE_NAME,
                       projection,
                       selection,
                       selectionArgs,
                       null,
                       null,
                       sortOrder
               );
               break;
           }
           case MOVIE_WITH_ID:
           {
               retCursor = getMoviesByID(uri,projection,sortOrder);
               break;
           }
           case MOST_POPULAR:
           {
               retCursor = getMoviesFromOption(MovieContract.PopularEntry.TABLE_NAME,
                                                                           projection,
                                                                            selection,
                                                                            selectionArgs,
                                                                           sortOrder);
               break;
           }
           case HIGHEST_RATED:
           {
               retCursor = getMoviesFromOption(MovieContract.HighestRatedEntry.TABLE_NAME,
                                                                               projection,
                                                                               selection,
                                                                               selectionArgs,
                                                                               sortOrder);
               break;

           }
           case FAVORITES:
           {
               retCursor = getMoviesFromOption(MovieContract.FavoritesEntry.TABLE_NAME,
                                                                               projection,
                                                                               selection,
                                                                               selectionArgs,
                                                                               sortOrder);
               break;
           }
       }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOST_POPULAR:
                return MovieContract.PopularEntry.CONTENT_DIR_TYPE;
            case HIGHEST_RATED:
                return MovieContract.HighestRatedEntry.CONTENT_DIR_TYPE;
            case FAVORITES:
                return MovieContract.FavoritesEntry.CONTENT_DIR_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase mDataBase = mMovieHelper.getReadableDatabase();
        Cursor dbCursor = mDataBase.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri = null; // URI to be returned
        long id;
        switch (match) {
            case MOVIES:
            {
                id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME,
                                                                          null, values,
                                                        SQLiteDatabase.CONFLICT_REPLACE);
                if(id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(id);
                }
                break;
            }
            case MOST_POPULAR:
            {
                    id = db.insert(MovieContract.PopularEntry.TABLE_NAME, null, values);
                    if (id > 0) {
                        returnUri = MovieContract.PopularEntry.CONTENT_URI;
                    } else {
                        throw new android.database.SQLException(SQL_INSERT_ERROR + uri);
                    }
                    break;
            }
            case HIGHEST_RATED:
            {
                    id = db.insert(MovieContract.HighestRatedEntry.TABLE_NAME, null, values);
                    if (id > 0) {
                        returnUri = MovieContract.HighestRatedEntry.CONTENT_URI;
                    } else {
                        throw new android.database.SQLException(SQL_INSERT_ERROR + uri);
                    }
                    break;
            }
            case FAVORITES:
            {
                    id = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, values);
                    if (id > 0) {
                        returnUri = MovieContract.FavoritesEntry.CONTENT_URI;
                    } else {
                        throw new android.database.SQLException(SQL_INSERT_ERROR + uri);
                    }
                    break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        switch (match) {
            case MOVIES:
            {
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                                                                    selection,
                                                                    selectionArgs);
                break;
            }
            case MOVIE_WITH_ID:
            {
                long id = MovieContract.MovieEntry.getIdFromUri(uri);
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                                                            MOVIE_ID_SELECTION,
                                                            new String[]{Long.toString(id)});
                break;
            }
            case MOST_POPULAR:
            {
                rowsDeleted = db.delete(MovieContract.PopularEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case HIGHEST_RATED:
            {
                rowsDeleted = db.delete(MovieContract.HighestRatedEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case FAVORITES:
            {
                rowsDeleted = db.delete(MovieContract.FavoritesEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:
            {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MOVIES: {
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case MOST_POPULAR:
            {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insertWithOnConflict(MovieContract.PopularEntry.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            }
            case HIGHEST_RATED:
            {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insertWithOnConflict(MovieContract.
                                                                HighestRatedEntry.TABLE_NAME,
                                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
    public Cursor getMoviesByID(Uri uri, String [] projection, String sortOrder) {
        long id = MovieContract.MovieEntry.getIdFromUri(uri);
        String[] selectionArgs = new String[]{Long.toString(id)};
        String selection = MOVIE_ID_SELECTION;

        return mMovieHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);


    }

    /*
     * it takes the movies from depending on the menu option
     *
     */
    public Cursor getMoviesFromOption(String tableName, String [] projection,
                                                            String selection,
                                                        String[] selectionArgs,
                                                            String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        // tableName INNER JOIN movies ON tableName.movie_id = movies._id
        sqLiteQueryBuilder.setTables(
                tableName + " INNER JOIN " + MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + tableName + "." + MovieContract.COLUMN_MOVIE_ID_FKEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID
        );

        return sqLiteQueryBuilder.query(mMovieHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
}
