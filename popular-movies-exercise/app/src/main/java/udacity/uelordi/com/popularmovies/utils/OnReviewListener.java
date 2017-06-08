package udacity.uelordi.com.popularmovies.utils;

import java.util.List;

import udacity.uelordi.com.popularmovies.content.ReviewContent;

/**
 * Created by uelordi on 18/04/2017.
 */

public interface OnReviewListener {
    void OnReviewListAvailable(List<ReviewContent> result);
}
