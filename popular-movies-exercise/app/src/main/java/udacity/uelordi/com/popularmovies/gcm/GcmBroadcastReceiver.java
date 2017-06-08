package udacity.uelordi.com.popularmovies.gcm;

import android.accounts.Account;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.VideoListActivity;
import udacity.uelordi.com.popularmovies.database.MovieContract;

/**
 * Created by uelordi on 23/04/17.
 */

public class GcmBroadcastReceiver extends GcmListenerService {
    private static final String TAG = GcmBroadcastReceiver.class.getName();
    public static final int NOTIFICATION_ID = 1;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String senderId = getString(R.string.gcm_defaultSenderId);
        sendNotification("hello kease");
    }
    private void sendNotification(String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, VideoListActivity.class), 0);
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.common_ic_googleplayservices);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.powered_by_google_light)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Weather Alert!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

 /*GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(gcm.getMessageType(intent)) &&
        intent.getExtras().containsKey("com.example.restaurant.SYNC_REQ")) {
            Log.d(TAG, "GCM sync notification! Requesting DB sync for server dbversion " + intent.getStringExtra("dbversion"));
            ContentResolver.requestSync(new Account("dummyaccount", context.getString(R.string.sync_account_type)), MovieContract.AUTHORITY, Bundle.EMPTY);*/



}
