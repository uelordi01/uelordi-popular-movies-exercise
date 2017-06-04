package udacity.uelordi.com.popularmovies.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.database.MovieContract;
import udacity.uelordi.com.popularmovies.utils.PrefUtils;

/**
 * Created by uelordi on 30/04/17.
 */

public final class MoviesListTask {
    private static final String LOG_TAG = "MoviesListTask";
    //DEFINE HERE YOUR ERROR HANDLING:
    public static final int ERROR_OK = 0;
    public static final int ERROR_NO_NETWORK = 1;
    public static final int ERROR_NO_CONNECTIVITY = 2;
    public static final int ERROR_DB_EMPTY = 3;
    public static final int ERROR_FAVORITES_EMPTY = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
        ERROR_OK,
        ERROR_NO_NETWORK,
        ERROR_NO_CONNECTIVITY,
        ERROR_DB_EMPTY,
        ERROR_FAVORITES_EMPTY
    })
    public  @interface ErrorStatus{}


    public static synchronized void syncMoviesList(Context context) {

        // call to the get movieList key
        PrefUtils.setErrorStatus(context, ERROR_OK);
        if(PrefUtils.isOnline(context)) {
            List<MovieContentDetails> result = NetworkModule.getInstance()
                    .syncMoviesListByOption(context.getString(R.string.pref_sort_popular_value));
            fillMoviesTable(context, result);
            fillExtraTables(context, result, MovieContract.PopularEntry.CONTENT_URI);
            result = NetworkModule.getInstance()
                    .syncMoviesListByOption(context.getString(R.string.pref_sort_rated_value));
            fillMoviesTable(context, result);
            fillExtraTables(context, result, MovieContract.HighestRatedEntry.CONTENT_URI);
        } else {
            //todo check if the database is empty
            PrefUtils.setErrorStatus(context, ERROR_NO_NETWORK);
        }
    }
    private static void fillMoviesTable(Context context, List<MovieContentDetails> result){
        List<ContentValues> contentMoviesList = new ArrayList<ContentValues>();


        for (Iterator<MovieContentDetails> i = result.iterator(); i.hasNext();) {
            MovieContentDetails item = i.next();
            contentMoviesList.add(item.toContentValues());
        }
        ContentValues[] dbinput = new ContentValues[contentMoviesList.size()];
        contentMoviesList.toArray(dbinput);
        context.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI,
                dbinput);
        Log.v(LOG_TAG,context.getString(R.string.log_movies_inserted));

    }
    private static void fillExtraTables(Context context,
                                 List<MovieContentDetails> result,
                                 Uri tableUri){

        List<ContentValues>  contentExtra = new ArrayList<ContentValues>();

        for (Iterator<MovieContentDetails> i = result.iterator(); i.hasNext();) {
            MovieContentDetails item = i.next();
            ContentValues extra = new ContentValues();
                extra.put(MovieContract.COLUMN_MOVIE_ID_KEY,item.getId());
            contentExtra.add(extra);
            }
        ContentValues[] dbinput = new ContentValues[contentExtra.size()];
        contentExtra.toArray(dbinput);
        context.getContentResolver().bulkInsert(tableUri,
                dbinput);
        Log.v(LOG_TAG, context.getString(R.string.log_extra_table_inserted, tableUri));
    }
}
