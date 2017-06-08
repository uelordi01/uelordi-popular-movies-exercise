package udacity.uelordi.com.popularmovies.utils;

import java.util.List;

import udacity.uelordi.com.popularmovies.content.ReviewContent;
import udacity.uelordi.com.popularmovies.content.TrailerContent;

/**
 * Created by uelordi on 18/04/2017.
 */

public interface OnTrailerListener {
    void OnTrailerListAvailable(List<TrailerContent> result);
}
