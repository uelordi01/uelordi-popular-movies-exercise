package udacity.uelordi.com.popularmovies.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.database.MovieContract;

/**
 * Created by uelordi on 30/04/17.
 */

public class MoviesListTask {
    private static final String LOG_TAG = "MoviesListTask";
    public static synchronized void syncMoviesList(Context context) {

        // call to the get movieList key
        List<MovieContentDetails> result = NetworkModule.getInstance()
                    .syncMoviesListByOption(context.getString(R.string.pref_sort_popular_value));
        fillMoviesTable(context, result);
        fillExtraTables(context,result, MovieContract.PopularEntry.CONTENT_URI);

        result = NetworkModule.getInstance()
                .syncMoviesListByOption(context.getString(R.string.pref_sort_rated_value));
        fillMoviesTable(context, result);
        fillExtraTables(context,result, MovieContract.HighestRatedEntry.CONTENT_URI);
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
