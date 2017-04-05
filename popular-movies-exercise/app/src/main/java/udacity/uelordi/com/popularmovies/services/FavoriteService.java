package udacity.uelordi.com.popularmovies.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.database.MovieContract;
import udacity.uelordi.com.popularmovies.database.MoviesProvider;

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
        // insert the movie:
        //store the image in the disk:

        mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                movieObject.toContentValues());
    }
    public void removeFromFavorites(MovieContentDetails movieObject)
    {
        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry._ID + "=" + movieObject.getMovieID(),
                null);
    }
    public boolean isFavorite(MovieContentDetails movieObject)
    {
        boolean favorite = false;
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry._ID + " = " + movieObject.getMovieID(),
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
