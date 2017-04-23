package udacity.uelordi.com.popularmovies.gcm;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.database.MovieContract;

/**
 * Created by uelordi on 23/04/17.
 */

public class GcmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = GcmBroadcastReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(gcm.getMessageType(intent)) &&
        intent.getExtras().containsKey("com.example.restaurant.SYNC_REQ")) {
            Log.d(TAG, "GCM sync notification! Requesting DB sync for server dbversion " + intent.getStringExtra("dbversion"));
            ContentResolver.requestSync(new Account("dummyaccount", context.getString(R.string.sync_account_type)), MovieContract.AUTHORITY, Bundle.EMPTY);
        }

    }
}
