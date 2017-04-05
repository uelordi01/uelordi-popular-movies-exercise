package udacity.uelordi.com.popularmovies.background;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Created by uelordi on 29/03/2017.
 */

public abstract class AbstractCursorLoader extends  CursorLoader {
    public AbstractCursorLoader(Context context) {
        super(context);
    }
    protected abstract CursorLoader buildList();
}
