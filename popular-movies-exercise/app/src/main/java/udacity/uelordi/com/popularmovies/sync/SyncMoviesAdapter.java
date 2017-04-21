package udacity.uelordi.com.popularmovies.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by uelordi on 22/04/17.
 */

public class SyncMoviesAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver mContentResolver;
    public SyncMoviesAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    public SyncMoviesAdapter(Context applicationContext, boolean b) {
        super(applicationContext,b);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

    }
}
