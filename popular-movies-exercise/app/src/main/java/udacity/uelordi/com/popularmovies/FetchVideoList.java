package udacity.uelordi.com.popularmovies;


import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.services.NetworkModule;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;
/**
 * Created by uelordi on 04/03/17.
 * The asynctask which holds the internet connection in background
 */
public class FetchVideoList extends AsyncTask<String,Void,List<MovieContentDetails>>
{
    private onFetchResults m_callback=null;
    private final String TAG = FetchVideoList.class.getSimpleName();
    @Override
    protected List<MovieContentDetails> doInBackground(String... sortedBy) {
//        try {
//            //return NetworkModule.getInstance().getMovieList(sortedBy[0]);
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    protected void onPostExecute(List<MovieContentDetails> s) {
        super.onPostExecute(s);
        if(m_callback!=null)
        {
            m_callback.OnListAvailable(s);
        }
        else
        {
            Log.e(TAG,"you must define the callback interface with setListener function");
        }

    }
    public void setListener(onFetchResults listener)
    {
        m_callback=listener;
    }
}