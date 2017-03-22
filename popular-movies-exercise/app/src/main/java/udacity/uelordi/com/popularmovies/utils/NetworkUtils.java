/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package udacity.uelordi.com.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static NetworkUtils m_instance;

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIE_DB_URL =
            "http://api.themoviedb.org/3";

    private final static String api_key="37683374c36effc22bc99f0d2bdf476c";
    private static String SORT_PARAM = "sort_by";
    private final static String API_KEY = "api_key";
    private final static String APPEND_TO_KEY = "append_to_response" ;
    private final static String VALUES_TO_RESPONSE="videos,reviews";
    private final static String MOVIE_ID_PARAM="movie";

    //private final static String MOVIE_

    public enum ACTION_TYPE{GET_MOVIE_LIST,GET_REVIEWS,GET_TRAILERS}
    private static ACTION_TYPE mCurrentActionType;

    //TODO 2 IMPLEMENT THE HTTP AND HTTPS LIBRARY http://loopj.com/android-async-http/
    //TODO 1 CREATE THE FUNCTION TO OBTAIN THE COMMENTS AND THE TRAILER LINKS.
    //TODO 3 CHANGE THE URLS OF THE REVIEWS AND THE MOVIES:  (FIND THE PATHS PLEASE)

    private NetworkUtils() {};
    public static synchronized NetworkUtils getInstance()
    {
        if(m_instance == null)
        {
            m_instance = new NetworkUtils();
        }
        return m_instance;
    }
    public String getMovieList(String sort) throws IOException {
        String url=STATIC_MOVIE_DB_URL+"/discover/movie";

        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(SORT_PARAM, sort)
                .appendQueryParameter(API_KEY, api_key)
                .build();
        String urld=builtUri.toString();
        String result= callToHttp(new URL(urld));
        return result;
    }
    public String getMovieDetails(int movieID) throws IOException {
        String url=STATIC_MOVIE_DB_URL+"/"+MOVIE_ID_PARAM+"/"+movieID;

        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .appendQueryParameter(APPEND_TO_KEY,VALUES_TO_RESPONSE)
                .build();
        String urld=builtUri.toString();
        String result= callToHttp(new URL(urld));
        return result;
    }

    public String callToHttp(URL url) throws IOException {
        boolean success;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}