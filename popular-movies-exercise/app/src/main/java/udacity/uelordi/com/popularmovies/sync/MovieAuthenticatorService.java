package udacity.uelordi.com.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by uelordi on 22/04/17.
 */

public class MovieAuthenticatorService extends Service {
    private MovieAuthenticator mAuth;
    @Override
    public void onCreate() {
        super.onCreate();
        mAuth =  new MovieAuthenticator(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuth.getIBinder();

    }
}
