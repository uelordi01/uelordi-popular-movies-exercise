package udacity.uelordi.com.popularmovies.utils;

import java.util.List;

import udacity.uelordi.com.popularmovies.content.MovieContentDetails;

/**
 * Created by uelordi on 04/03/17.
 */

public interface onFetchResults {
     void OnListAvailable(List<MovieContentDetails> result);
}
