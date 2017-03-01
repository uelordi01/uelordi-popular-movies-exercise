package udacity.uelordi.com.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.VideosJSONUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;
/**
 * Created by uelordi on 04/03/17.
 */

public class FetchVideoList extends AsyncTask<String,Void,String>
{
    private onFetchResults m_callback=null;
    private final String TAG = FetchVideoList.class.getSimpleName();
    @Override
    protected String doInBackground(String... sortedBy) {
        String result=null;
        try {
            URL url= NetworkUtils.buildUrl(sortedBy[0]);
            result=NetworkUtils.getResponseFromHttpUrl(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(m_callback!=null)
        {
            try {
                m_callback.OnListAvailable(VideosJSONUtils.getMovieListFromJson(s));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //m_callback.OnListAvailable(s);
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