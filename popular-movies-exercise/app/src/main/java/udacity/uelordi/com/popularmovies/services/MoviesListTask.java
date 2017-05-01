package udacity.uelordi.com.popularmovies.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import java.util.List;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;

/**
 * Created by uelordi on 30/04/17.
 */

public class MoviesListTask {
    public static synchronized void syncMoviesList(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(context);
        String defaultValue=sharedPreferences.getString(context.
                        getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_popular_value));

        // call to the get movieList key
        List<MovieContentDetails> result = NetworkModule.getInstance()
                                                .syncMoviesListByOption(defaultValue);

        //  make here the bulk insert of movies.

    }
}
