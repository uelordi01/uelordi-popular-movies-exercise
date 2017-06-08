package udacity.uelordi.com.popularmovies.services;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.database.MovieContract;



/**
 * Created by uelordi on 28/03/17.
 */

public class FavoriteService {
    private static final String TAG = "FavoriteService";
    private static FavoriteService m_instance = null;
    private FavoriteDatabaseTask mFavoriteTask = null;
    private FavoriteListener mListener = null;
    private boolean mForceActionPerform;

    public interface FavoriteListener {
         void onIsFavorite(boolean result, boolean performAction);
         void onAddFavoriteCompleted(boolean result);
         void onRemoveFavoriteCompleted(boolean result);
         void onFavoriteError(String error);
    };

    public void registerContentResolver(ContentResolver cr, FavoriteListener listener) {
        mFavoriteTask = new FavoriteDatabaseTask(cr);
        mListener = listener;
        mForceActionPerform = false;
    }
    public static FavoriteService getInstance()
    {
        if(m_instance == null)
        {
            m_instance = new FavoriteService();
        }
        return m_instance;
    }
    private class FavoriteDatabaseTask extends AsyncQueryHandler {

        public FavoriteDatabaseTask(ContentResolver cr) {
            super(cr);
        }
        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            boolean isFavorite = false;
            if (cursor != null) {
                if(cursor.getCount() > 0 ) {
                    isFavorite = true;
                } else {
                    isFavorite = false;
                }

                cursor.close();
                mListener.onIsFavorite(isFavorite, mForceActionPerform);
            } else {
                //mListener.onError("Error cursor not defined");
            }
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
            super.onUpdateComplete(token, cookie, result);
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            super.onInsertComplete(token, cookie, uri);
            mListener.onAddFavoriteCompleted(true);
            Log.v(TAG,"the favorite service works perfectly");
        }

        @Override
        protected void onDeleteComplete(int token, Object cookie, int result) {
            super.onDeleteComplete(token, cookie, result);
            mListener.onRemoveFavoriteCompleted(true);
            // TODO IMPLEMENT THE RESULT CASE TO THROW THE ERROR
        }

    }

    public void addToFavorites(MovieContentDetails movieObject) {
        ContentValues mCV = new ContentValues();
        mCV.put(MovieContract.COLUMN_MOVIE_ID_KEY,movieObject.getId());

        mFavoriteTask.startInsert(1,null,MovieContract.FavoriteEntry.CONTENT_URI,mCV);
    }
    public void removeFromFavorites(MovieContentDetails movieObject) {
        mFavoriteTask.startDelete(2,null,MovieContract.FavoriteEntry.CONTENT_URI,
                MovieContract.COLUMN_MOVIE_ID_KEY + "=" + movieObject.getId(),
                null);
    }
    public void checkFavorite(MovieContentDetails movieObject, boolean performAction) {
        mForceActionPerform = performAction;
        mFavoriteTask.startQuery(3, null,
                MovieContract.FavoriteEntry.CONTENT_URI,
                null,
                MovieContract.COLUMN_MOVIE_ID_KEY + " = " + movieObject.getId(),
                null,
                null);
    }
}
