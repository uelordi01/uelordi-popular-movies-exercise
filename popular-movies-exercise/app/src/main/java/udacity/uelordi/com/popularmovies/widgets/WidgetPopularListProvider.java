package udacity.uelordi.com.popularmovies.widgets;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import udacity.uelordi.com.popularmovies.MovieDetailsActivity;
import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.VideoListActivity;

/**
 * Created by uelordi on 06/06/2017.
 */

public class WidgetPopularListProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_movie_popular_list);

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_item_list_pickable, pendingIntent);

            // Set up the collection
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views, appWidgetId);
            } else {
                setRemoteAdapterV11(context, views, appWidgetId);
            }
            boolean useListWidget = context.getResources()
                    .getBoolean(R.bool.widget_list_enabled);
            Intent clickIntentTemplate = useListWidget
                    ? new Intent(context, MovieDetailsActivity.class)
                    : new Intent(context, VideoListActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_popular_grv_view, clickPendingIntentTemplate);
            views.setEmptyView(R.id.widget_popular_grv_view, R.id.widget_empty);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views, int appWidgetId) {
        Intent adapterIntent = new Intent(context,StockWidgetService.class);
        adapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.widget_popular_grv_view, adapterIntent);
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views, int appWidgetId) {
        Intent adapterIntent = new Intent(context,StockWidgetService.class);
        adapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(0, R.id.widget_popular_grv_view,
                adapterIntent);
    }
}
