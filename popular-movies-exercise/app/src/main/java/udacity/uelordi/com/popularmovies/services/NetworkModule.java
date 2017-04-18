package udacity.uelordi.com.popularmovies.services;

import android.content.Context;
import android.database.Observable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacity.uelordi.com.popularmovies.BuildConfig;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;

/**
 * Created by uelordi on 18/04/2017.
 */

public class NetworkModule {
    private static final String STATIC_MOVIE_DB_URL =
            "http://api.themoviedb.org/3/";
    private final static String IMAGE_URL_PATH = "http://image.tmdb.org/t/p/w185";
    private final static String YOUTUBE_BASE_URL="http://www.youtube.com/watch?v=";
    private static String api_key= BuildConfig.API_KEY;
    private static final String TAG = "NetworkModule";

    private static NetworkModule mInstance;
    private Retrofit.Builder builder;
    private Retrofit retrofit;
    private DBMovieServiceEndPoint client;
    private onFetchResults m_callback=null;


    public NetworkModule() {
        configureNetworkModule();
    }

    private void configureNetworkModule() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        builder =
                new Retrofit.Builder()
                        .baseUrl(STATIC_MOVIE_DB_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );
        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        client =  retrofit.create(DBMovieServiceEndPoint.class);
    }
    public void configureCallback(onFetchResults callback) {
        m_callback = callback;
    }
    public void getMovieList(String sort_by) {
        Call<MovieListResponse> call = client.getMovieList(sort_by,api_key);
        call.enqueue(
                new Callback<MovieListResponse>() {
                    @Override
                    public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                        List<MovieContentDetails> list = response.body().getResults();
                        if(m_callback != null) {
                            m_callback.OnListAvailable(list);
                        }
                        else
                        {
                            Log.e(TAG,"No callback defined");
                        }

                    }

                    @Override
                    public void onFailure(Call<MovieListResponse> call, Throwable t) {
                        Log.e(TAG,t.getMessage());
                    }
                }
        );
    }
    public static  NetworkModule getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new NetworkModule();
        }
        return mInstance;
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public static String getImageUrlPah() {
        return IMAGE_URL_PATH;
    }
    public static String getYoutubePath() {
        return YOUTUBE_BASE_URL;
    }

}
