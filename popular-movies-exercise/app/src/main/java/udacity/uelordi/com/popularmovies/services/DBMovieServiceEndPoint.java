package udacity.uelordi.com.popularmovies.services;

/**
 * Created by uelordi on 17/04/2017.
 */
import android.database.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface DBMovieServiceEndPoint {
    @GET("movie/{id}/videos")
    Observable<TrailerResponse> getMovieVideos(@Path("id") long movieId);

    @GET("movie/{id}/reviews")
    Observable<ReviewResponse> getMovieReviews(@Path("id") long movieId);

    @GET("movie/{sort_by}")
    Observable<MovieListResponse> getMovieReviews(@Path("sort_by") String sortBy);
}
