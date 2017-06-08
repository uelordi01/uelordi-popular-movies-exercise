package udacity.uelordi.com.popularmovies.services;

/**
 * Created by uelordi on 17/04/2017.
 */
import android.database.Observable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;

public interface DBMovieServiceEndPoint {
    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieVideos(@Path("id") long movieId, @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") long movieId, @Query("api_key") String api_key);

    @GET("movie/{sort_by}")
    Call<MovieListResponse> getMovieList(@Path("sort_by") String sortBy, @Query("api_key") String api_key);
//    Observable<MovieListResponse>

}
