package udacity.uelordi.com.popularmovies.background;

import android.content.Context;
import org.json.JSONException;
import java.io.IOException;
import java.util.List;
import udacity.uelordi.com.popularmovies.utils.MovieDetailJSONUtils;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;

/**
 * Created by uelordi on 21/03/17.
 */

public class MovieDetailTaskLoader extends AbstractMovieTaskLoader {
    private final long mVideoId;

    public MovieDetailTaskLoader(Context context, long id) {
        super(context);
        mVideoId=id;
    }

    @Override
    public List buildList() {
        String result = null;
        try {
            result = NetworkUtils.getInstance().getMovieDetails(mVideoId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return MovieDetailJSONUtils.getMovieDetail(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
