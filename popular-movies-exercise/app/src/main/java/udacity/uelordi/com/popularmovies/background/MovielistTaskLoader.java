package udacity.uelordi.com.popularmovies.background;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.VideosJSONUtils;

/**
 * Created by uelordi on 20/03/17.
 */

public class MovielistTaskLoader extends AbstractMovieTaskLoader {
    private String mSortedBy;

    public MovielistTaskLoader(Context context, String sortedBy)
    {
        super(context);
        mSortedBy=sortedBy;

    }
    @Override
    protected List buildList()  {
        String result = null;
        try {
            result = NetworkUtils.getInstance().getMovieList(mSortedBy);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return VideosJSONUtils.getMovieListFromJson(result);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
