package udacity.uelordi.com.popularmovies.background;

import android.content.ContentValues;
import android.content.Context;

import android.content.CursorLoader;
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

public class MovielistTaskLoader  {
    private String mSortedBy;
    private Context mContext;
    private Uri currentUri = null;
    private List<MovieContentDetails> mMovieContentList;
    private final String PROJECTIONS[] = {MovieContract.MovieEntry.COLUMN_IMAGE_URL};

    public Uri getCurrentUri() {
        return currentUri;
    }

    public void setCurrentUri(Uri currentUri) {
        this.currentUri = currentUri;
    }

    public String[] getPROJECTIONS() {
        return PROJECTIONS;
    }

    public MovielistTaskLoader(Context context, String sortedBy)
    {
        mContext = context;
        mSortedBy=sortedBy;
    }


    public CursorLoader buildCursor() {
        String result = null;
        Uri queryURI = null;

        try {
            result = NetworkUtils.getInstance().getMovieList(mSortedBy);
        } catch (IOException e) {
            e.printStackTrace();
        }

            List<MovieContentDetails> movieList = mMovieContentList;
            ContentValues responseMovies[] = new ContentValues[movieList.size()];
            ContentValues responseJoinedTable[] = new ContentValues[movieList.size()];
            for(int i=0; i < movieList.size(); i++) {
                responseMovies[i] = movieList.get(i).toContentValues();
                responseJoinedTable[i] = createJoinedTableContentValues(movieList.get(i));
            }
            int rowsCreated = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.
                                                                CONTENT_URI,responseMovies);
            /*if(rowsCreated > 0) {

            }*/
            if(mSortedBy.equals(mContext.getResources().
                                        getString(R.string.pref_sort_popular_value))) {
                mContext.getContentResolver().bulkInsert(
                                                MovieContract.HighestRatedEntry.CONTENT_URI,
                                                responseJoinedTable);
                currentUri = MovieContract.PopularEntry.CONTENT_URI;
            }
            else if(mSortedBy.equals(mContext.getResources().
                    getString(R.string.pref_sort_rated_value))){
                mContext.getContentResolver().bulkInsert(
                        MovieContract.HighestRatedEntry.CONTENT_URI,
                        responseJoinedTable);
                currentUri = MovieContract.HighestRatedEntry.CONTENT_URI;
            }
            else {
                return null;
            }

        return new CursorLoader(
                mContext,
                currentUri,
                PROJECTIONS,
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
