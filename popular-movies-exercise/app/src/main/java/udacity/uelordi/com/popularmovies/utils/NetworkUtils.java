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
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import udacity.uelordi.com.popularmovies.BuildConfig;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static NetworkUtils m_instance;

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIE_DB_URL =
            "http://api.themoviedb.org/3";

    private static String api_key= BuildConfig.API_KEY;
    private final static String SORT_PARAM = "sort_by";
    private final static String API_KEY = "api_key";
    private final static String APPEND_TO_KEY = "append_to_response" ;
    private final static String VALUES_TO_RESPONSE="videos,reviews";
    private final static String MOVIE_ID_PARAM="movie";


    private NetworkUtils() {}
    public static synchronized NetworkUtils getInstance()
    {
        if(m_instance == null)
        {
            m_instance = new NetworkUtils();
        }
        return m_instance;
    }
    public String getMovieList(String sort) throws IOException {
        String url=STATIC_MOVIE_DB_URL+"/movie/"+sort;

        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();
        String urld=builtUri.toString();
        return callToHttp(new URL(urld));
    }
    public String getMovieDetails(long movieID) throws IOException {
        String url=STATIC_MOVIE_DB_URL+"/"+MOVIE_ID_PARAM+"/"+movieID;

        Uri builtUri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .appendQueryParameter(APPEND_TO_KEY,VALUES_TO_RESPONSE)
                .build();
        String urld=builtUri.toString();
        return callToHttp(new URL(urld));

    }

    public String callToHttp(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}