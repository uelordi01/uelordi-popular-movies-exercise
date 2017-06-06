package udacity.uelordi.com.popularmovies.widgets;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.services.MoviesListTask;

/**
 * Created by uelordi on 06/06/2017.
 */

public class TodayWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context,SmalWidgetIntenService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if( intent.getAction().equals(MoviesListTask.ACTION_DATA_UPDATED)) {
            context.startService(new Intent(context,SmalWidgetIntenService.class));
        }
        // TODO FINISH THE CONTROL OF THE BROADCAST RECEIVER-


    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context,SmalWidgetIntenService.class));
    }


}
