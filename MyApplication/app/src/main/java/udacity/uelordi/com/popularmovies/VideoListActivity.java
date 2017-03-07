package udacity.uelordi.com.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.v7.app.AppCompatActivity;
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

import udacity.uelordi.com.popularmovies.content.MovieContent;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;

public class VideoListActivity extends AppCompatActivity implements onFetchResults,VideoListAdapter.ListItemClickListener {
    private static final String TAG = VideoListActivity.class.getSimpleName();

    private FetchVideoList mVideoListTask;
    private TextView mErrorView;
    private ProgressBar mVideoListProgressBar;

    private RecyclerView mMovieList;
    private VideoListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        String filter=getResources().getString(R.string.action_popular);

        mVideoListProgressBar=(ProgressBar)findViewById(R.id.pg_movie_list);

        mMovieList=(RecyclerView)findViewById(R.id.rv_movie_list);

        hideErrorMessage();

        if(NetworkUtils.isOnline(getApplicationContext()))
        {
            mVideoListTask=new FetchVideoList();
            mVideoListTask.setListener(this);
            showLoadingBar();
            mVideoListTask.execute(filter);
        }
        else
        {
            showErrorMessage();
        }


    }

    @Override
    public void OnListAvailable(List<MovieContent> result) {
        hideLoadingBar();
        GridLayoutManager gridManager=new GridLayoutManager(VideoListActivity.this,3);
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
        if(mErrorView==null)
        {
            mErrorView=(TextView) findViewById(R.id.connectivity_error);
        }
        mErrorView.setVisibility(View.VISIBLE);
    }
    private void hideErrorMessage()
    {
        if(mErrorView==null)
        {
            mErrorView=(TextView) findViewById(R.id.connectivity_error);
        }
        mErrorView.setVisibility(View.INVISIBLE);
    }
    private void showLoadingBar()
    {
        if(mVideoListProgressBar==null)
        {
            mVideoListProgressBar=(ProgressBar)findViewById(R.id.pg_movie_list);
        }
        mVideoListProgressBar.setVisibility(View.VISIBLE);

    }
    private void hideLoadingBar()
    {
        if(mVideoListProgressBar==null)
        {
            mVideoListProgressBar=(ProgressBar)findViewById(R.id.pg_movie_list);
        }
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
                if(NetworkUtils.isOnline(getApplicationContext())) {
                    mVideoListTask = new FetchVideoList();
                    mVideoListTask.setListener(this);
                    showLoadingBar();
                    mVideoListTask.execute(getResources().getString(R.string.action_popular));
                }
                break;
            }
            case R.id.action_filter_rated:
            {
                Log.v(TAG,"filter_rated");
                if(NetworkUtils.isOnline(getApplicationContext())) {
                    mVideoListTask = new FetchVideoList();
                    mVideoListTask.setListener(this);
                    showLoadingBar();
                    mVideoListTask.execute(getResources().getString(R.string.action_rating));
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(MovieContent movie) {
        Intent my_intent = new Intent(this,MovieDetails.class);
        my_intent.putExtra("poster_path",movie.getPoster_path());
        my_intent.putExtra("title",movie.getTitle());
        my_intent.putExtra("synopsys",movie.getSynopsis());
        my_intent.putExtra("user_rating",movie.getUser_rating());
        my_intent.putExtra("release_date",movie.getRelease_date());
        startActivity(my_intent);
    }
}
