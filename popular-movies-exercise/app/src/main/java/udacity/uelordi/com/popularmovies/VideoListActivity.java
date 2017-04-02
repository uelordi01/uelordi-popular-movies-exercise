package udacity.uelordi.com.popularmovies;



import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.uelordi.com.popularmovies.adapters.OnItemClickListener;
import udacity.uelordi.com.popularmovies.adapters.VideoListAdapter;
import udacity.uelordi.com.popularmovies.background.MovielistTaskLoader;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.content.TrailerContent;
import udacity.uelordi.com.popularmovies.preferences.SettingsActivity;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;

public class VideoListActivity extends AppCompatActivity implements
                                                            OnItemClickListener,
                                                            onFetchResults,
                                                            LoaderManager.LoaderCallbacks<Cursor>,
                                                SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = VideoListActivity.class.getSimpleName();

    private FetchVideoList mVideoListTask;
    @BindView (R.id.connectivity_error) TextView mErrorView;
    @BindView (R.id.pg_movie_list)  ProgressBar mVideoListProgressBar;
    @BindView (R.id.rv_movie_list) RecyclerView mMovieList;

    private VideoListAdapter mMovieListAdapter;

    private static final String SELECTED_SEARCH_OPTION = "search_option";
    private static final String RESULT_LIST_KEY = "vide_list_result";
    private static final int MOVIES_LOADER_TASK_ID = 5;
    private static final int FAVORITES_MOVIES_LOADER_TASK_ID = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        ButterKnife.bind(this);
        String filter=getResources().getString(R.string.action_popular);
        hideErrorMessage();

        if(NetworkUtils.isOnline(getApplicationContext()))
        {
            showLoadingBar();
            Bundle queryBundle = new Bundle();
            queryBundle.putString(SELECTED_SEARCH_OPTION,setupPreferences());
            if(queryBundle.getString(SELECTED_SEARCH_OPTION).
                    equals(getResources().getString(R.string.pref_sort_favorites_value))) {

            }
            else {
                //
                mVideoListTask = new FetchVideoList();
                mVideoListTask.setListener(this);
                mVideoListTask.execute(queryBundle.getString(SELECTED_SEARCH_OPTION));
            }

        }
        else
        {
            showErrorMessage();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }
    private void showErrorMessage()
    {
        mErrorView.setVisibility(View.VISIBLE);
    }
    private void hideErrorMessage()
    {
        mErrorView.setVisibility(View.INVISIBLE);
    }
    private void showLoadingBar()
    {
        mVideoListProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideLoadingBar()
    {
        mVideoListProgressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemWasClicked=item.getItemId();
        switch (itemWasClicked)
        {
            case R.id.action_settings:
            {
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        String selected_option=args.getString(SELECTED_SEARCH_OPTION);
        List<MovieContentDetails> result = args.getParcelableArrayList(RESULT_LIST_KEY);
        switch (loaderID) {
            case FAVORITES_MOVIES_LOADER_TASK_ID:
            {
              // Cursor MyCursor =
               // return new  MovielistTaskLoader(this,selected_option).buildCursor();
                /*MovielistTaskLoader task = new MovielistTaskLoader(this,selected_option);
                task.buildCursor();*/
                new MovielistTaskLoader(this, selected_option, result).buildCursor();
                /*return new CursorLoader(
                        this,
                        task.getCurrentUri(),
                        task.getPROJECTIONS(),
                        null,
                        null,
                        null);*/


            }
            case MOVIES_LOADER_TASK_ID:
            {
                //return new MovielistTaskLoader(this,selected_option);

                /*return new CursorLoader(
                        this,
                        task.getCurrentUri(),
                        task.getPROJECTIONS(),
                        null,
                        null,
                        null);*/
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderID);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        hideLoadingBar();
    }
//
//    public void onLoadFinished(Loader<List> loader, List data) {
//        hideLoadingBar();
//        GridLayoutManager gridManager=new GridLayoutManager(VideoListActivity.this,2);
//        mMovieList.setLayoutManager(gridManager);
//        mMovieListAdapter=new VideoListAdapter(VideoListActivity.this);
//        mMovieListAdapter.setMovieList(data);
//        mMovieList.setAdapter(mMovieListAdapter);
//
//    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public void restartLoader(String action_type)
    {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SELECTED_SEARCH_OPTION,action_type);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> videoListLoader = loaderManager.getLoader(MOVIES_LOADER_TASK_ID);
        if ( videoListLoader == null ) {
            loaderManager.initLoader(MOVIES_LOADER_TASK_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER_TASK_ID, queryBundle, this);
        }
    }
    /*
     setup the preferences and return the default value for the preferences
     */
    public String setupPreferences()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue=sharedPreferences.getString(getString(R.string.pref_sort_key),
                                                    getString(R.string.pref_sort_popular_value));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        return defaultValue;
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        restartLoader(key);
    }

    @Override
    public void onItemClick(MovieContentDetails movie) {
//        Intent my_intent = new Intent(this,MovieDetailsActivity.class);
//        long id=movie.getMovieID();
//        my_intent.putExtra("movieid",id);
//        my_intent.putExtra("poster_path",movie.getPoster_path());
//        my_intent.putExtra("title",movie.getTitle());
//        my_intent.putExtra("synopsys",movie.getSynopsis());
//        my_intent.putExtra("user_rating",movie.getUser_rating());
//        my_intent.putExtra("release_date",movie.getRelease_date());
//        startActivity(my_intent);
    }

    @Override
    public void onItemClick(String movieID) {
        Intent my_intent = new Intent(this,MovieDetailsActivity.class);
        String id = movieID;
        my_intent.putExtra("movieid",id);
    }

    public void loadFavoritesList() {

    }
    @Override
    public void onItemClick(TrailerContent content) {
        //TODO IMPLEMENT THE TRAILER ADAPTER PART:
    }

    @Override
    public void OnListAvailable(List<MovieContentDetails> result) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SELECTED_SEARCH_OPTION,setupPreferences());
        queryBundle.putParcelableArrayList(RESULT_LIST_KEY, (ArrayList<? extends Parcelable>) result);
        getSupportLoaderManager().initLoader(MOVIES_LOADER_TASK_ID, queryBundle, this);
    }
}
