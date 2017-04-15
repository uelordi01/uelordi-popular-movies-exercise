package udacity.uelordi.com.popularmovies;



import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import udacity.uelordi.com.popularmovies.adapters.OnVideoItemClickListener;
import udacity.uelordi.com.popularmovies.adapters.VideoListAdapter;
import udacity.uelordi.com.popularmovies.background.FavoriteTaskLoader;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.preferences.SettingsActivity;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;

public class VideoListActivity extends AppCompatActivity implements
                                            OnVideoItemClickListener,
                                            onFetchResults,
                                            LoaderManager.LoaderCallbacks<Cursor>,
                                SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = VideoListActivity.class.getSimpleName();


    @BindView (R.id.connectivity_error) TextView mErrorView;
    @BindView (R.id.pg_movie_list)  ProgressBar mVideoListProgressBar;
    @BindView (R.id.rv_movie_list) RecyclerView mMovieList;
    private int mPosition = RecyclerView.NO_POSITION;

    private VideoListAdapter mMovieListAdapter;
    private static final int FAVORITES_MOVIES_LOADER_TASK_ID = 6;
    private final static String VIDEO_LIST_KEY = "saved_movie_list";
    private static Bundle mBundleRecyclerViewState;

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
    protected void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mMovieList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(VIDEO_LIST_KEY, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(VIDEO_LIST_KEY);
            mMovieList.getLayoutManager().onRestoreInstanceState(listState);
        }
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
        mMovieListAdapter.addData(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mMovieList.smoothScrollToPosition(mPosition);
        mMovieList.setAdapter(mMovieListAdapter);
        hideLoadingBar();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieListAdapter.swapCursor(null);
    }

    public void startLoader()
    {
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
        getMovieList(key);
    }

    @Override
    public void onItemClick(MovieContentDetails movie) {
        Intent detail_activity = new Intent(this,MovieDetailsActivity.class);
        detail_activity.putExtra(MovieDetailsActivity.VIDEO_OBJECT_KEY,movie);
        startActivity(detail_activity);
    }

    public void setAdapters(){
        // TODO: change your grid layout manager from staggered layout
        // https://inducesmile.com/android/android-staggeredgridlayoutmanager-example-tutorial/
        // it seems that staggered is an asymetric gridView.
        int numViewsForRow = 0;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            numViewsForRow = 2;
        } else {
            numViewsForRow = 4;
        }
        GridLayoutManager gridManager = new GridLayoutManager(VideoListActivity.this,numViewsForRow);
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
        FetchVideoList mVideoListTask;
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
    private void getMovieList(String key) {
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
                        hideLoadingBar();
                        showErrorMessage();
                    }
            }
        }
    }
}
