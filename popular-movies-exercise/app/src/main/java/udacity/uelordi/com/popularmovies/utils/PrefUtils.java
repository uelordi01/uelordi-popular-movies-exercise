package udacity.uelordi.com.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import udacity.uelordi.com.popularmovies.R;

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
}
