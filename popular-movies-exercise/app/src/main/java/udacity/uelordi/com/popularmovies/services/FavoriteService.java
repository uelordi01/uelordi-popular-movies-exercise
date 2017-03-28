package udacity.uelordi.com.popularmovies.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.database.MovieContract;

/**
 * Created by uelordi on 28/03/17.
 */

public class FavoriteService {
    Context mContext;
    private static FavoriteService m_instance= null;
    public static FavoriteService getInstance()
    {
        if(m_instance == null)
        {
            m_instance = new FavoriteService();
        }
        return m_instance;
    }
    public void setContext(Context context)
    {
        mContext=context.getApplicationContext();
    }
    public void addToFavorites(MovieContentDetails movieObject)
    {
        mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                                                    movieObject.toContentValues());
        ContentValues content = new ContentValues();
        content.put(MovieContract.COLUMN_MOVIE_ID_FKEY,movieObject.getMovieID());
        mContext.getContentResolver().insert(MovieContract.FavoritesEntry.CONTENT_URI,content);
    }
    public void removeFromFavorites(MovieContentDetails movieObject)
    {
        mContext.getContentResolver().delete(MovieContract.FavoritesEntry.CONTENT_URI,
                MovieContract.COLUMN_MOVIE_ID_FKEY + "=" + movieObject.getMovieID(),
                null);
    }
    public boolean isFavorite(MovieContentDetails movieObject)
    {
        boolean favorite = false;
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.FavoritesEntry.CONTENT_URI,
                null,
                MovieContract.COLUMN_MOVIE_ID_FKEY + " = " + movieObject.getMovieID(),
                null,
                null
        );
        if (cursor != null) {
            if(cursor.getCount() > 0 ) {
                favorite = true;
            }

            cursor.close();
        }
        return favorite;
    }
}
