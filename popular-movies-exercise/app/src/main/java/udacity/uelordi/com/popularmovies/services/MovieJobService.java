package udacity.uelordi.com.popularmovies.services;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by uelordi on 04/06/2017.
 */

public class MovieJobService extends JobService {
    private static final String LOG_TAG = "MovieJobService";
    @Override
    public boolean onStartJob(JobParameters job) {
        Log.v(LOG_TAG, "onStartJob");
        MovieListUtils.initialize(getApplicationContext());
        return true;
    }
    @Override
    public boolean onStopJob(JobParameters job) {
        Log.v(LOG_TAG, "onStopJob");
        return false;
    }
}
