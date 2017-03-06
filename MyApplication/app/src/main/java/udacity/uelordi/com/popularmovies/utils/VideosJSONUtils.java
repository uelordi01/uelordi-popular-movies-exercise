package udacity.uelordi.com.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import udacity.uelordi.com.popularmovies.content.MovieContent;

/**
 * Created by uelordi on 28/02/2017.
 */

public class VideosJSONUtils {
    public static List<MovieContent> getMovieListFromJson(String json_string) throws JSONException
    {
        final String RESULTS_LIST="results";
        final String ID="id";
        final String TITLE="title";
        final String RELEASE_DATE="release_date";
        final String VOTE_AVERAGE="vote_average";
        final String SYNOPSYS="overview";
        final String IMAGE_PATH="poster_path";

        JSONObject movie_Json=new JSONObject(json_string);

        List<MovieContent> result_array= new ArrayList<>();
        //if(movie_Json.has())
        //TODO LOOK IF THE RESULTING IMAGE IS NULL
        JSONArray jresult_list= movie_Json.getJSONArray(RESULTS_LIST);
        for(int i=0;i<jresult_list.length();i++){
            JSONObject jobject = jresult_list.getJSONObject(i);
            MovieContent mc=new MovieContent();
            mc.setTitle(jobject.getString(TITLE));
            mc.setPoster_path(jobject.getString(IMAGE_PATH));
            mc.setRelease_date(jobject.getString(RELEASE_DATE));
            mc.setUser_rating(jobject.getString(VOTE_AVERAGE));
            mc.setSynopsis(jobject.getString(SYNOPSYS));
            result_array.add(mc);

        }
        return result_array;
    }

}
