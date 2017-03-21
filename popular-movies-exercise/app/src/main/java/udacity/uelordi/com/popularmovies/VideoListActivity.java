package udacity.uelordi.com.popularmovies;


import android.content.Intent;

import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.uelordi.com.popularmovies.background.MovielistTaskLoader;
import udacity.uelordi.com.popularmovies.content.MovieContent;
import udacity.uelordi.com.popularmovies.preferences.SettingsActivity;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;

public class VideoListActivity extends AppCompatActivity implements onFetchResults,
                                                            VideoListAdapter.ListItemClickListener,
                                                            LoaderManager.LoaderCallbacks<List>,
                                                SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = VideoListActivity.class.getSimpleName();

    private FetchVideoList mVideoListTask;
    @BindView (R.id.connectivity_error) TextView mErrorView;
    @BindView (R.id.pg_movie_list)  ProgressBar mVideoListProgressBar;
    @BindView (R.id.rv_movie_list) RecyclerView mMovieList;

    private VideoListAdapter mMovieListAdapter;

    private static final String SELECTED_SEARCH_OPTION = "search_option";

    private static final int LOADER_TASK_ID=5;



    //TODO make the preferences without preference_activity
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
            //queryBundle.putString(SELECTED_SEARCH_OPTION,filter);
            getSupportLoaderManager().initLoader(LOADER_TASK_ID, queryBundle, this);
        }
        else
        {
            showErrorMessage();
        }


    }

    @Override
    public void OnListAvailable(List<MovieContent> result) {
        hideLoadingBar();
        GridLayoutManager gridManager=new GridLayoutManager(VideoListActivity.this,2);
        mMovieList.setLayoutManager(gridManager);
        mMovieListAdapter=new VideoListAdapter(VideoListActivity.this,result);
        mMovieList.setAdapter(mMovieListAdapter);
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
            case R.id.action_filter_popular:
            {
                Log.v(TAG,"filter_popular");
                hideErrorMessage();
                if(NetworkUtils.isOnline(getApplicationContext())) {
                    /*mVideoListTask = new FetchVideoList();
                    mVideoListTask.setListener(this);*/
                    showLoadingBar();
                    restartLoader(getResources().getString(R.string.action_popular));
                    //mVideoListTask.execute(getResources().getString(R.string.action_popular));
                }
                break;
            }
            case R.id.action_filter_rated:
            {
                Log.v(TAG,"filter_rated");
                hideErrorMessage();
                if(NetworkUtils.isOnline(getApplicationContext())) {
                    /*mVideoListTask = new FetchVideoList();
                    mVideoListTask.setListener(this);*/
                    showLoadingBar();
                    restartLoader(getResources().getString(R.string.action_rating));
                    //mVideoListTask.execute(getResources().getString(R.string.action_rating));
                }
                break;
            }
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
    public void onListItemClick(MovieContent movie) {
        Intent my_intent = new Intent(this,MovieDetails.class);
        int id=movie.getMovieID();
        my_intent.putExtra("movieid",id);
        my_intent.putExtra("poster_path",movie.getPoster_path());
        my_intent.putExtra("title",movie.getTitle());
        my_intent.putExtra("synopsys",movie.getSynopsis());
        my_intent.putExtra("user_rating",movie.getUser_rating());
        my_intent.putExtra("release_date",movie.getRelease_date());
        startActivity(my_intent);
    }

    @Override
    public Loader<List> onCreateLoader(int id, Bundle args) {
        String selected_option=args.getString(SELECTED_SEARCH_OPTION);
        return new MovielistTaskLoader(this,selected_option);
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List data) {
        hideLoadingBar();
        // mMovieList=(RecyclerView) findViewById(R.id.rv_movie_list);
        GridLayoutManager gridManager=new GridLayoutManager(VideoListActivity.this,2);
        mMovieList.setLayoutManager(gridManager);
        mMovieListAdapter=new VideoListAdapter(VideoListActivity.this,data);
        mMovieList.setAdapter(mMovieListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List> loader) {

    }
    public void restartLoader(String action_type)
    {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SELECTED_SEARCH_OPTION,action_type);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> videoListLoader = loaderManager.getLoader(LOADER_TASK_ID);
        if ( videoListLoader == null ) {
            loaderManager.initLoader(LOADER_TASK_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER_TASK_ID, queryBundle, this);
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
        /*Bundle query =
        if( key.equals(getString(R.string.pref_sort_popular_value)) )
        {

        }
        if( key.equals(getString(R.string.pref_sort_rated_value)) ) {

        }
        if( key.equals(getString(R.string.pref_sort_favorites_value)) ) {

        }*/
    }
}
