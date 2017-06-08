package udacity.uelordi.com.popularmovies.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by uelordi on 30/04/17.
 */

public class MovieListIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MovieListIntentService() {
        super("MovieListIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MoviesListTask.syncMoviesList(this);
    }
}
