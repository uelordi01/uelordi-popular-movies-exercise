package udacity.uelordi.com.popularmovies.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by uelordi on 07/06/2017.
 */

public class StockWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new VideolistWidgetRemoteAdapter(this, intent);
    }
}
