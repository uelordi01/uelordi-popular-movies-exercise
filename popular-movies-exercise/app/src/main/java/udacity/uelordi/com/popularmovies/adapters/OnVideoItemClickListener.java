package udacity.uelordi.com.popularmovies.adapters;

import android.view.View;

import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.content.TrailerContent;

/**
 * Created by uelordi on 29/03/17.
 */

public interface OnVideoItemClickListener {
    void onItemClick(MovieContentDetails content);
    void onItemClick(String movieID);
}
