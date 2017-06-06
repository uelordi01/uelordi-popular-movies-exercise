package udacity.uelordi.com.popularmovies.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.VideoListActivity;

/**
 * Created by uelordi on 05/06/2017.
 */

public class NotificationUtils {
    private static final int NEW_VIDEOS_NOTIFICATION_ID = 3004;
    public static void notifyToTheUsers(Context context) {
        String notificationTitle = context.getString(R.string.app_name);
        String notificationText = context.getString(R.string.notification_text);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.common_ic_googleplayservices)
                .setLargeIcon(BitmapFactory
                        .decodeResource(context.getResources(),R.drawable.common_ic_googleplayservices))
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setAutoCancel(true);
        Intent iMainActivity = new Intent(context, VideoListActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(iMainActivity);
        PendingIntent pendingIntent = taskStackBuilder
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager nManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        nManager.notify(NEW_VIDEOS_NOTIFICATION_ID, notificationBuilder.build());


    }

    public static void clearAllNotifications(Context context){
        NotificationManager nM = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nM.cancelAll();
    }
}
