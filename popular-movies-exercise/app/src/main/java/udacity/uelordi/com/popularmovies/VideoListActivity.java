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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.uelordi.com.popularmovies.adapters.OnItemClickListener;
import udacity.uelordi.com.popularmovies.adapters.VideoListAdapter;
import udacity.uelordi.com.popularmovies.background.FavoriteTaskLoader;
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
    private int mPosition = RecyclerView.NO_POSITION;

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
        String defaultFilter = setupPreferences();
        hideErrorMessage();
        setAdapters();
        showLoadingBar();
        getMovieList(defaultFilter);
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
        switch (loaderID) {
            case FAVORITES_MOVIES_LOADER_TASK_ID:
            {
               return  new FavoriteTaskLoader(this).buildCursor();
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderID);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       // mMovieListAdapter.swapCursor(data);
        mMovieListAdapter.addData(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//      COMPLETED (30) Smooth scroll the RecyclerView to mPosition
        mMovieList.smoothScrollToPosition(mPosition);
        int count = data.getCount();
//      COMPLETED (31) If the Cursor's size is not equal to 0, call showWeatherDataView
        if (count != 0) hideLoadingBar();
        mMovieList.setAdapter(mMovieListAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieListAdapter.swapCursor(null);
    }

    public void startLoader()
    {
//        Bundle queryBundle = new Bundle();
//        queryBundle.putString(SELECTED_SEARCH_OPTION,checkSortingPreferences());
//        if(result != null) {
//            queryBundle.putParcelableArrayList(RESULT_LIST_KEY, (ArrayList<? extends Parcelable>) result);
//        }
        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<String> videoListLoader = loaderManager.getLoader(FAVORITES_MOVIES_LOADER_TASK_ID);
        if ( videoListLoader == null ) {
            getSupportLoaderManager().initLoader(FAVORITES_MOVIES_LOADER_TASK_ID, null, this);
        } else {
            loaderManager.restartLoader(FAVORITES_MOVIES_LOADER_TASK_ID, null, this);
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
        //restartLoader(key);
        getMovieList(key);
    }

    @Override
    public void onItemClick(MovieContentDetails movie) {
        Intent detail_activity = new Intent(this,MovieDetailsActivity.class);
        detail_activity.putExtra(MovieDetailsActivity.VIDEO_OBJECT_KEY,movie);
        startActivity(detail_activity);
    }

    @Override
    public void onItemClick(String movieID) {
        Intent movie_detail_intent = new Intent(this,MovieDetailsActivity.class);
        long id =Long.parseLong(movieID);
        movie_detail_intent.putExtra("movieid",id);
        startActivity(movie_detail_intent);
    }

    public void loadFavoritesList() {

    }
    @Override
    public void onItemClick(TrailerContent content) {
        //TODO IMPLEMENT THE TRAILER ADAPTER PART:
    }
    public void setAdapters(){

        GridLayoutManager gridManager=new GridLayoutManager(VideoListActivity.this,2);
        mMovieListAdapter = new VideoListAdapter(VideoListActivity.this);
        mMovieList.setLayoutManager(gridManager);
        mMovieList.setAdapter(mMovieListAdapter);
    }
    @Override
    public void OnListAvailable(List<MovieContentDetails> result) {
        mMovieListAdapter.addData(result);
        hideLoadingBar();
    }
    public void getMoviesFromTheInternet(String key) {
        mVideoListTask = new FetchVideoList();
        mVideoListTask.setListener(this);
        mVideoListTask.execute(key);
    }
    public String checkSortingPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue=sharedPreferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_value));
        return defaultValue;
    }
    public void getMovieList(String key) {
        if(key != null) {
            if (key.equals(getResources().getString(R.string.pref_sort_favorites_value))) {
                startLoader();
            } else {
                    if(NetworkUtils.isOnline(getApplicationContext())) {
                        getMoviesFromTheInternet(key);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.connectivity_warning),
                                Toast.LENGTH_SHORT).show();
                        showErrorMessage();
                    }
            }
        }
    }
}
