package udacity.uelordi.com.popularmovies.widgets;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.concurrent.ExecutionException;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.VideoListActivity;
import udacity.uelordi.com.popularmovies.database.MovieContract;
import udacity.uelordi.com.popularmovies.services.NetworkModule;

/**
 * Created by uelordi on 06/06/2017.
 */

public class SmalWidgetIntenService extends IntentService {

    public SmalWidgetIntenService() {
        super(SmalWidgetIntenService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                TodayWidgetProvider.class));
        String description = "test";

        // Get today's data from the ContentProvider

        Cursor data = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.MOVIE_COLUMNS, null,
                null, MovieContract.MovieEntry.COL_MOVIE_VOTE_AVERAGE + " ASC");
        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }
        int posterColumn = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URL);
        int titleColumn =  data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        String posterPath = data.getString(posterColumn);
        String movie_path = NetworkModule.getInstance().getImageUrlPah()
                + posterPath;

        String title = data.getString(titleColumn);
        data.close();
        for(int appWidgetID: appWidgetIds) {
            int layout_id = R.layout.widget_mini_layout;
            RemoteViews RV= new RemoteViews(getPackageName(),layout_id);


            Bitmap resultingView = null;
            try {
                resultingView = Glide.with(getApplicationContext())
                            .load( movie_path)
                            .asBitmap()
                            .into(50,50).get();
                RV.setImageViewBitmap(R.id.movie_best_image, resultingView);
//                RV.setTextViewText(R.id.widget_mini_film_title,title);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(RV, description);
            }
            Intent launchIntent = new Intent(this, VideoListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            RV.setOnClickPendingIntent(R.id.movie_best_image, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetID, RV);
            }
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription( android.support.design.R.drawable.abc_ic_star_black_36dp, description);
    }
}
