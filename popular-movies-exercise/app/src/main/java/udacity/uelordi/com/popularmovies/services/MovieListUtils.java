package udacity.uelordi.com.popularmovies.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import udacity.uelordi.com.popularmovies.utils.PrefUtils;

/**
 * Created by uelordi on 01/05/17.
 */

public class MovieListUtils {
    private static final String TAG = "MovieListUtils";
    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;
    private static final String REMINDER_JOB_TAG = "movies_reminder_tag";
    private static boolean sInitialized;
    public static void initialize(@NonNull final Context context) {
        // this class is the scheduler to initialize the synchronizing movies.
        if(sInitialized)
        //make the calls of the cursors and the table: make the query of the cursors

        //if the cursor is null startSyncInmediatelly, lo hace en un asynctask pero
        // yo lo haré con threads and handlers:
        PrefUtils.setErrorStatus(context, MoviesListTask.ERROR_OK);
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "initializing the synchronization");
                startSyncInmediatelly(context);
            }
        };
        new Thread(myRunnable).start();


    }
    public static void startSyncInmediatelly(@NonNull final Context context) {
        Intent startSyncIntent = new Intent(context, MovieListIntentService.class);
        context.startService(startSyncIntent);
    }
    public static synchronized void schedulePeriodic(Context context) {
        Log.d(TAG,"Scheduling a periodic task");
        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        /* Create the Job to periodically create reminders to drink water */
        Job constraintReminderJob = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences */
                .setService(MovieJobService.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
                .setTag(REMINDER_JOB_TAG)
                /*
                 * Network constraints on which this Job should run. In this app, we're using the
                 * device charging constraint so that the job only executes if the device is
                 * charging.
                 *
                 * In a normal app, it might be a good idea to include a preference for this,
                 * as different users may have different preferences on when you should be
                 * syncing your application's data.
                 */
                .setConstraints(Constraint.ON_ANY_NETWORK)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the reminders to happen every 15 minutes or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(constraintReminderJob);

        /* The job has been initialized */
        sInitialized = true;

    }

}
