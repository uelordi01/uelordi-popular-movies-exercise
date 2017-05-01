package udacity.uelordi.com.popularmovies.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by uelordi on 01/05/17.
 */

public class MovieListUtils {
    private static boolean sInitialized;
    private static final String TAG = "MovieListUtils";
    public static void initialize(@NonNull final Context context) {
        // this class is the scheduler to initialize the synchronizing movies.
        if(sInitialized) return;
        //make the calls of the cursors and the table: make the query of the cursors

        //if the cursor is null startSyncInmediatelly, lo hace en un asynctask pero
        // yo lo haré con threads and handlers:
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

}
