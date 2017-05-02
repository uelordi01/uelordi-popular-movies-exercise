package udacity.uelordi.com.popularmovies;



import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
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

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import udacity.uelordi.com.popularmovies.adapters.OnVideoItemClickListener;
import udacity.uelordi.com.popularmovies.adapters.VideoListAdapter;
import udacity.uelordi.com.popularmovies.background.FavoriteTaskLoader;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.database.MovieContract;
import udacity.uelordi.com.popularmovies.database.MoviesProvider;
import udacity.uelordi.com.popularmovies.preferences.SettingsActivity;
import udacity.uelordi.com.popularmovies.services.MovieListUtils;
import udacity.uelordi.com.popularmovies.services.NetworkModule;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;
// TODO implement a syncadapter.
// TODO implements the notifications.
// TODO IMPLEMENT THE ERROR HANDLING AS THE LATEST COURSERS. (SERVER DOWN, DESTINATION UNREACHABLE, DATABASE EMPTY)
// TODO IMPLEMENT BROADCAST RECEIVER.
// TODO IMPLEMENT THE STYLES.
// TODO IMPLEMENT GCM (GOOGLE CLOUD MESSAGE) WITH GOOGLE PLAY DRIVER()

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
    private final static String RECYCLER_VIEW_LIST_INDEX = "recycler_view_list_index";
    private final static String SORTING_EXTRA_PREF = "sort_pref";

    private static Bundle mBundleRecyclerViewState;
    GridLayoutManager mGridManager;
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String filter =setupPreferences();
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        if(savedInstanceState != null) {
                filter = savedInstanceState.getString(SORTING_EXTRA_PREF);
        }
        hideErrorMessage();
        initInterface();
        showLoadingBar();
        //TODO CREATE THE INTENT SERVICE PERFECTLY:
        MovieListUtils.initialize(this);
// getMovieList(filter);
    }
    private Account createDummyAccount(Context context) {
        Account dummyAccount = new Account("dummyaccount", "com.udacity.uelordi");
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(dummyAccount, null, null);
        ContentResolver.setSyncAutomatically(dummyAccount, MovieContract.AUTHORITY, true);
        return dummyAccount;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checkGooglePlayServices();
       // ContentResolver.requestSync(createDummyAccount(this), MovieContract.AUTHORITY, Bundle.EMPTY);
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
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mMovieList.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(VIDEO_LIST_KEY,mListState);
        String preference = checkSortingPreferences();
        outState.putString(SORTING_EXTRA_PREF,preference);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(VIDEO_LIST_KEY);
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
        mMovieList.getLayoutManager().onRestoreInstanceState(mListState);
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
        String defaultValue=sharedPreferences.getString(key,
                getString(R.string.pref_sort_popular_value));
        getMovieList(defaultValue);
    }

    @Override
    public void onItemClick(MovieContentDetails movie) {
        Intent detail_activity = new Intent(this,MovieDetailsActivity.class);
        detail_activity.putExtra(MovieDetailsActivity.VIDEO_OBJECT_KEY,movie);
        startActivity(detail_activity);
    }

    public void initInterface(){
        int numViewsForRow = 0;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            numViewsForRow = 2;
        } else {
            numViewsForRow = 3;
        }
        mGridManager = new GridLayoutManager(VideoListActivity.this,numViewsForRow);
        mMovieListAdapter = new VideoListAdapter(VideoListActivity.this);
        mMovieList.setLayoutManager(mGridManager);
        mMovieList.setAdapter(mMovieListAdapter);
    }
    @Override
    public void OnListAvailable(List<MovieContentDetails> result) {
        hideLoadingBar();
        if(result.size() > 0) {
            mMovieListAdapter.addData(result);
            mMovieList.getLayoutManager().onRestoreInstanceState(mListState);

        } else {

        }

    }
    public void getMoviesFromTheInternet(String key) throws IOException {
        NetworkModule.getInstance().configureCallback(this);
        try {
            NetworkModule.getInstance().getMovieList(key);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
                    if(NetworkModule.getInstance()
                            .isOnline(getApplicationContext())) {
                        try {
                            getMoviesFromTheInternet(key);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
