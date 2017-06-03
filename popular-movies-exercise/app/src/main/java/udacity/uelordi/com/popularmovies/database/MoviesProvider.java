package udacity.uelordi.com.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    public static final int MOVIE_POPULAR = 200;
    public static final int MOVIE_HIGHEST_RATED = 300;
    public static final int MOVIE_FAVORITE = 400;


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

       uriMatcher.addURI(authority, MovieContract.PATH_MOVIES_POPULAR, MOVIE_POPULAR);
       uriMatcher.addURI(authority, MovieContract.PATH_MOVIES_RATED, MOVIE_HIGHEST_RATED);
       uriMatcher.addURI(authority, MovieContract.PATH_MOVIES_FAVORITE, MOVIE_FAVORITE);


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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
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
           case MOVIE_POPULAR:
           {
               retCursor = getMoviesFromReferenceTable(MovieContract.PopularEntry.TABLE_NAME,
                                                        projection,
                       selection,
                       selectionArgs,
                       sortOrder);
               break;
           }
           case MOVIE_HIGHEST_RATED:
           {
               retCursor = getMoviesFromReferenceTable(MovieContract.HighestRatedEntry.TABLE_NAME,
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
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
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
            case MOVIE_POPULAR: {
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

            case MOVIE_HIGHEST_RATED: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insertWithOnConflict(MovieContract.HighestRatedEntry.TABLE_NAME,
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



            case MOVIE_FAVORITE: {
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insertWithOnConflict(MovieContract.FavoriteEntry.TABLE_NAME,
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
    private Cursor getMoviesByID(Uri uri, String [] projection, String sortOrder) {
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
    private Cursor getMoviesFromReferenceTable(String tableName, String[] projection, String selection,
                                              String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        sqLiteQueryBuilder.setTables(
                tableName + " INNER JOIN " + MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + tableName + "." + MovieContract.COLUMN_MOVIE_ID_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                                                    "." + MovieContract.MovieEntry._ID
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
