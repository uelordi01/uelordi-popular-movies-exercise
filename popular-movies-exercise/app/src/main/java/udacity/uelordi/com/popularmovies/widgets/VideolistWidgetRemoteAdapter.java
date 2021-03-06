package udacity.uelordi.com.popularmovies.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.database.MovieContract;

/**
 * Created by uelordi on 06/06/2017.
 */

public class VideolistWidgetRemoteAdapter implements
    RemoteViewsService.RemoteViewsFactory {
    private Cursor data;
    private final int mWidgetId;
    private final Context mContext;

    public VideolistWidgetRemoteAdapter(Context context,
                                        Intent intent) {
        mContext = context;
        mWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
// nothing todo
    }

    @Override
    public void onDataSetChanged() {
        if (data != null) {
            data.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        data = mContext.getContentResolver().query(MovieContract.PopularEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null,
                null,
                null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                data == null || !data.moveToPosition(position)) {
            return null;
        }
        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_movie_popular_list);
        if (data.moveToPosition(position)) {
            int movieTitleindex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);//Contract.Quote.COLUMN_SYMBOL);
            String movieTitle = data.getString(movieTitleindex);
            /*int priceColumn = data.getColumnIndex(Contract.Quote.COLUMN_PRICE);
            int percentageColumn = data.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE);
            int absChangeColumn = data.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE);

            float priceRaw = data.getFloat(priceColumn);
            float percentageRaw = data.getFloat(percentageColumn);
            float absChangeRaw = data.getFloat(absChangeColumn);
            String symbol = data.getString(symbolColumn);
            String priceValue = dollarFormat.format(priceRaw);
            String percentageValue =percentageFormat.format(percentageRaw);
            if(absChangeRaw > 0 ) {
                views.setInt(R.id.tv_widget_stock_change_percentage,
                        "setBackgroundResource",
                        R.drawable.percent_change_pill_green);
            } else {
                views.setInt(R.id.tv_widget_stock_change_percentage,
                        "setBackgroundResource",
                        R.drawable.percent_change_pill_red);
            }*/
            views.setTextViewText(R.id.widget_id_title, movieTitle);
//            views.setTextViewText(R.id.tv_widget_stock_price, priceValue);
//            views.setTextViewText(R.id.tv_widget_stock_change_percentage, percentageValue);

            final Intent fillInIntent = new Intent();
//            fillInIntent.putExtra(mContext.getString(R.string.pref_stocks_key),symbol);
            fillInIntent.setData(MovieContract.PopularEntry.CONTENT_URI);
            views.setOnClickFillInIntent(R.id.widget_popular_grv_view, fillInIntent);
        }
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
