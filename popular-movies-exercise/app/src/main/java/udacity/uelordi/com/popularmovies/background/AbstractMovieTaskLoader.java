package udacity.uelordi.com.popularmovies.background;

import android.content.Context;
import android.content.CursorLoader;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by uelordi on 20/03/17.
 */

public abstract class AbstractMovieTaskLoader extends  AsyncTaskLoader<List> {
    protected List mLastDataList=null;

    public AbstractMovieTaskLoader(Context context) {
        super(context);
    }

    protected abstract List buildList();

    @Override
    public void deliverResult(
            List dataList) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            emptyDataList(dataList);
            return;
        }
        List oldDataList = mLastDataList;
        mLastDataList = dataList;
        if (isStarted()) {
            super.deliverResult(dataList);
        }
        if (oldDataList != null && oldDataList != dataList
                && oldDataList.size() > 0) {
            emptyDataList(oldDataList);
        }

    }
    protected void emptyDataList(List dataList) {
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                dataList.remove(i);
            }
        }
    }


    @Override
    public List loadInBackground() {
        return buildList();
    }

    @Override
    protected void onStartLoading() {
        if (mLastDataList != null) {
            deliverResult(mLastDataList);
        }
        if (takeContentChanged() || mLastDataList == null
                || mLastDataList.size() == 0) {
            forceLoad();
        }
    }

    @Override
    public void onCanceled(List dataList) {
        if (dataList != null && dataList.size() > 0) {
            emptyDataList(dataList);
        }
    }

    @Override
    protected void onStopLoading() {
       cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        if (mLastDataList != null && mLastDataList.size() > 0) {
            emptyDataList(mLastDataList);
        }
        mLastDataList = null;
    }
}
