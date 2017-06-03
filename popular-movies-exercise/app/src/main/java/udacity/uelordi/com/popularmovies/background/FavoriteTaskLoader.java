package udacity.uelordi.com.popularmovies.background;


import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.net.Uri;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.database.MovieContract;
import udacity.uelordi.com.popularmovies.utils.PrefUtils;

/**
 * Created by uelordi on 20/03/17.
 */

public class FavoriteTaskLoader {
    private final Context mContext;
    private Uri currentUri = null;
    private final String PROJECTIONS[] = {MovieContract.MovieEntry.COLUMN_IMAGE_URL,MovieContract.MovieEntry.COLUMN_TITLE};



    public String[] getPROJECTIONS() {
        return PROJECTIONS;
    }

    public FavoriteTaskLoader(Context context)
    {

        mContext = context;
        if(PrefUtils.getCurrentMovieTypeOption(context)
                .equals(context.getString(R.string.pref_sort_popular_value))) {
            currentUri = MovieContract.PopularEntry.CONTENT_URI;
        } else {
            currentUri = MovieContract.HighestRatedEntry.CONTENT_URI;
        }
    }


    public CursorLoader buildCursor() {
        return new CursorLoader(
                mContext,
                currentUri,
                null,
                null,
                null,
                null);
    }
}
