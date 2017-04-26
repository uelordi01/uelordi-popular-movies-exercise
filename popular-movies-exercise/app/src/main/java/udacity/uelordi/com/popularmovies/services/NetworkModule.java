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
import udacity.uelordi.com.popularmovies.adapters.OnReviewItemListener;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.utils.OnReviewListener;
import udacity.uelordi.com.popularmovies.utils.OnTrailerListener;
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
    private OnReviewListener m_rev_callback;
    private OnTrailerListener m_trai_callback;


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
    public void configureCallback(OnReviewListener listener){
        m_rev_callback = listener;
    }
    public void configureCallback(OnTrailerListener listener){
        m_trai_callback = listener;
    }
    public void getMovieList(String sort_by) {
        Call<MovieListResponse> call = client.getMovieList(sort_by,api_key);
        call.enqueue(
                new Callback<MovieListResponse>() {
                    @Override
                    public void onResponse(Call<MovieListResponse> call,
                                           Response<MovieListResponse> response) {
                        if(response.isSuccessful()) {
                            List<MovieContentDetails> list = response.body().getResults();
                            if(m_callback != null) {
                                m_callback.OnListAvailable(list);
                            }
                            else
                            {
                                Log.e(TAG,"No callback defined");
                            }
                        } else {
                            Log.e(TAG,"unknown_error");
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieListResponse> call, Throwable t) {
                        Log.e(TAG,t.getMessage());
                    }
                }
        );
    }
    public void getReviewList(long movieID) {
        Call<ReviewResponse> call = client.getMovieReviews(movieID,api_key);
        call.enqueue(
                new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call,
                                           Response<ReviewResponse> response) {
                        if(m_rev_callback != null) {
                            m_rev_callback.OnReviewListAvailable(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        Log.e(TAG,t.getMessage());
                    }
                }
        );
    }
    public void getTrailerList(long movieID) {
        Call<TrailerResponse> call = client.getMovieVideos(movieID,api_key);
        call.enqueue(
            new Callback<TrailerResponse>() {

                @Override
                public void onResponse(Call<TrailerResponse> call,
                                       Response<TrailerResponse> response) {
                    if(m_trai_callback != null) {
                        m_trai_callback.OnTrailerListAvailable(response.body().getResults());
                    }
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {

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
