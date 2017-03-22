package udacity.uelordi.com.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import udacity.uelordi.com.popularmovies.content.MovieContentDetails;

/**
 * Created by uelordi on 28/02/2017.
 */

public class MovieDetailJSONUtils {
    public static List<MovieContentDetails> getMovieDetail(String json_string) throws JSONException
    {
        final String RESULTS_LIST="results";
        final String ID="id";
        final String TITLE="title";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";
        final String SYNOPSYS = "overview";
        final String IMAGE_PATH = "poster_path";
        final String REVIEW_LIST = "reviews";
        final String TRAILER_LIST = "videos";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String TRAILER_ID = "id";
        final String TRAILER_VIDEO_KEY = "key";
        final String TRAILER_VIDEO_NAME = "name";


        JSONObject movie_Json=new JSONObject(json_string);

        List<MovieContentDetails> result_array= new ArrayList<>();
        String image_path=movie_Json.getString(IMAGE_PATH);

        MovieContentDetails mc=new MovieContentDetails();
        mc.setMovieID(movie_Json.getInt(ID));
        mc.setTitle(movie_Json.getString(TITLE));
        mc.setPoster_path(image_path);
        mc.setRelease_date(movie_Json.getString(RELEASE_DATE));
        mc.setUser_rating(movie_Json.getString(VOTE_AVERAGE));
        mc.setSynopsis(movie_Json.getString(SYNOPSYS));

        JSONObject review_object = movie_Json.getJSONObject(REVIEW_LIST);
        JSONObject trailer_object = movie_Json.getJSONObject(TRAILER_LIST);

        JSONArray jRevResult_list= review_object.getJSONArray(RESULTS_LIST);
        JSONArray jTraiResult_list= trailer_object.getJSONArray(RESULTS_LIST);

        for(int i = 0; i< jRevResult_list.length();i++)
        {
            JSONObject jobject = jRevResult_list.getJSONObject(i);
            mc.addReview(jobject.getString(REVIEW_AUTHOR),jobject.getString(REVIEW_CONTENT));
        }
        for(int i = 0; i< jRevResult_list.length();i++)
        {
            JSONObject jobject = jTraiResult_list.getJSONObject(i);
            mc.addTrailer(jobject.getString(TRAILER_ID),
                          jobject.getString(TRAILER_VIDEO_KEY),
                          jobject.getString(TRAILER_VIDEO_NAME));
        }
        result_array.add(mc);
        return result_array;
    }

}
