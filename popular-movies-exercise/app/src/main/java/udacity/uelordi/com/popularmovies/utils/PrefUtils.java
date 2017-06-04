package udacity.uelordi.com.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.services.MoviesListTask;

/**
 * Created by uelordi on 03/06/2017.
 */

public class PrefUtils {
    public static String getCurrentMovieTypeOption(Context context) {
        String key = context.getString(R.string.pref_sort_key);
        String defaultValue = context.getString(R.string.pref_sort_popular_value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, defaultValue);
    }
    public static void setMovieTypeOption(Context context, String value) {
        String key = context.getString(R.string.pref_sort_key);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void setErrorStatus(Context context, @MoviesListTask.ErrorStatus int error) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(context.getString(R.string.current_error_key),error);
        editor.apply();
    }
    @SuppressWarnings("ResourceType")
    static public @MoviesListTask.ErrorStatus
    int getErrorStatus(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
       return prefs.getInt(context.getString(R.string.current_error_key), MoviesListTask.ERROR_OK);
    }
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
