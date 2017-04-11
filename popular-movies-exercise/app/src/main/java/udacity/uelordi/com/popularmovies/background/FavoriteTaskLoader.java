package udacity.uelordi.com.popularmovies.background;


import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.net.Uri;
import udacity.uelordi.com.popularmovies.database.MovieContract;

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
        currentUri = MovieContract.MovieEntry.CONTENT_URI;
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
