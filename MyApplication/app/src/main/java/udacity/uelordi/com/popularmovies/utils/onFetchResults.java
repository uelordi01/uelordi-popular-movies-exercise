package udacity.uelordi.com.popularmovies.utils;

import java.util.List;

import udacity.uelordi.com.popularmovies.content.MovieContent;

/**
 * Created by uelordi on 04/03/17.
 */

public interface onFetchResults {
    public void OnListAvailable(List<MovieContent> result);
   // public void OnError(String msg);
}
