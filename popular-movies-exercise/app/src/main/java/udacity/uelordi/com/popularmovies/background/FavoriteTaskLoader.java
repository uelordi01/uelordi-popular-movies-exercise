package udacity.uelordi.com.popularmovies.background;

import android.content.ContentValues;
import android.content.Context;

import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;


import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.database.MovieContract;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.VideosJSONUtils;

/**
 * Created by uelordi on 20/03/17.
 */

public class FavoriteTaskLoader {
    private String mSortedBy;
    private Context mContext;
    private Uri currentUri = null;
    private final String PROJECTIONS[] = {MovieContract.MovieEntry.COLUMN_IMAGE_URL,MovieContract.MovieEntry.COLUMN_TITLE};

    public Uri getCurrentUri() {
        return currentUri;
    }

    public void setCurrentUri(Uri currentUri) {
        this.currentUri = currentUri;
    }

    public String[] getPROJECTIONS() {
        return PROJECTIONS;
    }

    public FavoriteTaskLoader(Context context)
    {
        mContext = context;
        currentUri = MovieContract.MovieEntry.CONTENT_URI;
    }


    public CursorLoader buildCursor() {;
        return new CursorLoader(
                mContext,
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }
    public ContentValues createJoinedTableContentValues(MovieContentDetails detail) {
        ContentValues content = new ContentValues();
        content.put(MovieContract.COLUMN_MOVIE_ID_FKEY,detail.getMovieID());
        return content;
    }
    //@Override
    /*protected List buildList()  {
        String result = null;

    }*/


}
