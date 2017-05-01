package udacity.uelordi.com.popularmovies.sync;

import android.content.Context;
import android.support.annotation.NonNull;


import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import java.util.concurrent.TimeUnit;

/**
 * Created by uelordi on 30/04/17.
 */

public class ReminderUtilities {
    private static final int REMINDER_INTERVAL_MINUTES = 15;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.
                toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    private static final String REMINDER_JOB_TAG = "movies_reminder_tag";
    private static boolean sInitialized;

    synchronized public static void scheduleMoviesReminder(@NonNull final Context context) {
        if(sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

       /* Job constraintReminderJob =  dispatcher.newJobBuilder().
                setService();*/

    }
}
