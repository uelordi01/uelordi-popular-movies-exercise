package udacity.uelordi.com.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by uelordi on 22/04/17.
 */

public class SyncMoviesService extends Service {
    private SyncMoviesAdapter mAdapter;
    private static final String TAG = "SyncMoviesService";
    private final static Object sSyncAdapterLock = new Object();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"creating a syncAdapter");
        synchronized(sSyncAdapterLock) {
            if(mAdapter == null) {
                mAdapter = new SyncMoviesAdapter(getApplicationContext(),true);
            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAdapter.getSyncAdapterBinder();
    }
}
